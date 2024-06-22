import {useEffect, useState} from "react";
import Consultation from "./Consultation";
import {USER_TYPE} from "../services/utils";
import {GetAppointmentById, UpdateAppointmentStatus} from "../services/appointmentsServices";


function PatientAppointment(props) {

    const [updateCount, setUpdateCount] = useState(0);

    const [appointmentData, setAppointmentData] = useState({
        patient: {
            cnp: null, firstName: null, lastName: null,
        }, physician: {
            idPhysician: null,
        }, date: null, appointmentStatus: null
    })


    useEffect(() => {
        GetAppointmentById(USER_TYPE.PHYSICIAN, props.appointment.patient.cnp, props.appointment.physician.idPhysician, props.appointment.date).then(data => {
            setAppointmentData(data);
        }).catch(error => {
            alert(error)
        })
    }, [updateCount])

    function updateAppointment() {
        const selectedIndex = document.getElementsByName('appointmentStatusSelect')[0].selectedIndex;
        const appointmentStatus = selectedIndex === 0 ? 'ATTENDED' : 'NATTENDED';
        UpdateAppointmentStatus(appointmentData.patient.cnp, appointmentData.physician.idPhysician,
            appointmentData.date, appointmentStatus).then(() => {
            setUpdateCount(updateCount + 1);
        }).catch(error => {
            alert(error)
        })
    }

    return (<>
        {(appointmentData != null) &&
            <div className={'appointments'}>
                <p>{appointmentData.date}</p>
                {<p>Status: {appointmentData.appointmentStatus ? appointmentData.appointmentStatus : "Pending"}</p>}
                {
                    (appointmentData.appointmentStatus == null) &&
                    <>
                        <select name={'appointmentStatusSelect'}>
                            <option>Attended</option>
                            <option>Not attended</option>
                        </select>
                        <button onClick={updateAppointment}>Update appointment</button>
                    </>
                }
                {appointmentData?.appointmentStatus === "ATTENDED" &&
                    <Consultation appointment={appointmentData} userType={USER_TYPE.PHYSICIAN}/>}
            </div>
        }
    </>);
}

export default PatientAppointment;