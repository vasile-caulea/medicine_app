import {useEffect, useState} from "react";
import Consultation from "./Consultation";
import {UpdateAppointmentStatus} from "../services/appointmentsServices";

function MyAppointment(props) {

    const [appointmentData, setAppointmentData] = useState(
        {
            patient: {
                cnp: null
            },
            physician: {
                idPhysician: null,
                firstName: null,
                lastName: null,
                specialization: null
            },
            date: null,
            appointmentStatus: null
        }
    )
    const [button, setButton] = useState(null)
    const [firstRender, setFirstRender] = useState(false)
    useEffect(() => {
        if (!firstRender)
        {
            setAppointmentData(props.appointment)
            setFirstRender(true)
        }
        if (props.appointment.appointmentStatus == null && appointmentData.appointmentStatus == null) {
            setButton(<button onClick={() => cancelAppointment()}>Cancel appointment</button>)
        }
    }, [props.appointment, appointmentData])


    function cancelAppointment() {
        UpdateAppointmentStatus(appointmentData.patient.cnp, appointmentData.physician.idPhysician,
            appointmentData.date, 'cancel').then(() => {
            setButton(null)
            appointmentData.appointmentStatus = 'CANCELED'
            setAppointmentData(appointmentData)
        }).catch(error => {
            alert(error)
        })
    }

    return (
        <div className={"appointments"}>
            <p>{appointmentData && appointmentData.physician.specialization} appointment</p>
            <p>Date: {appointmentData && appointmentData.date}</p>
            <p>Physician: {appointmentData && appointmentData.physician.lastName + " " + appointmentData.physician.firstName}</p>
            <p>Status: {appointmentData && appointmentData.appointmentStatus ? appointmentData.appointmentStatus : "Pending"}</p>
            {button}
            {appointmentData && appointmentData.appointmentStatus === "ATTENDED" &&
                <div>
                    <Consultation appointment={appointmentData}/>
                </div>
            }
        </div>
    )
}

export default MyAppointment;