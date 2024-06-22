import {useLocation} from "react-router-dom";
import {LocalizationProvider} from '@mui/x-date-pickers/LocalizationProvider';
import {AdapterDayjs} from '@mui/x-date-pickers/AdapterDayjs';
import {DatePicker} from '@mui/x-date-pickers/DatePicker';
import {useEffect, useState} from "react";
import {styled} from "@mui/material";
import {PickersLayout} from "@mui/x-date-pickers";
import {AddAppointment, GetAppointmentDates} from "../services/appointmentsServices";
import dayjs from "dayjs";
import {generateHours} from "../services/utils";


const styledPickersLayout = styled(PickersLayout)({
    '.MuiDateCalendar-root': {
        borderRadius: 0,
        borderWidth: 0,
        borderColor: '#2196f3',
        border: '0px solid',
    }
})

function PhysicianDetails(props) {
    const location = useLocation()
    const physician = location.state.physician
    const [availableHours, setAvailableHours] = useState([])
    const [date, setDate] = useState(dayjs(new Date()));

    useEffect(() => {
        checkAppointments(date)
    }, []);

    function checkAppointments(date) {
        setAvailableHours([])
        setDate(date)
        const formattedDate = dayjs(date).format('YYYY-MM-DD');
        GetAppointmentDates(physician.idPhysician, formattedDate).then(data => {
            if (data['_embedded']) {
                const time = data['_embedded']['timestampList']
                setAvailableHours(generateHours(time))
            } else {
                setAvailableHours(generateHours([date.valueOf()]))
            }
        }).catch(error => {
            alert(error)
        })
    }

    function makeApointmentCallback(e) {
        e.preventDefault()
        if (!props.userState.loggedIn) {
            alert('Please login first')
            return
        }
        if (!props.patient) {
            alert('Please create your patient profie first')
            return
        }
        const hour = availableHours[document.getElementsByName('selectHours')[0].selectedIndex]
        AddAppointment(props.patient.cnp, physician.idPhysician, date, hour).then(() => {
            alert('Appointment created successfully')
        }).catch(error => {
            alert(error)
        })
    }

    return (<div>
            <p>{physician.firstName} {physician.lastName}</p>
            <p>Email: {physician.email}</p>
            <p>Phone number: {physician['phoneNumber']}</p>
            <p>Specialization: {physician['specialization']}</p>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
                <DatePicker slots={{layout: styledPickersLayout}}
                            label="Pick date"
                            value={date}
                            onChange={(newDate) => checkAppointments(newDate)}
                />
            </LocalizationProvider>
            {availableHours.length > 0 ? <select name={'selectHours'} className={'select-hours'}>
                {availableHours.map((hour, index) => <option key={index}>{hour}</option>)}
            </select> : <p>No available hours</p>}
            <button onClick={(e) => makeApointmentCallback(e)}>Make appointment</button>
        </div>);
}

export default PhysicianDetails;