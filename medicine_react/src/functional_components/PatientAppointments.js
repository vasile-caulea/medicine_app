import {useLocation} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {GetMyPatientAppointments} from "../services/appointmentsServices";
import MyAppointment from "./MyAppointment";
import PatientAppointment from "./PatientAppointment";


function PatientAppointments(props) {
    const location = useLocation();
    const {state} = location;

    const [appointments, setAppointments] = useState(null);

    useEffect(() => {
        GetMyPatientAppointments(state.patient.cnp, state.physician.idPhysician).then(data => {
            const embedded = data['_embedded'];
            if (!embedded || !embedded['appointmentList']) {
                setAppointments([]);
                return;
            }
            const appointmentsList = data['_embedded']['appointmentList']
                .sort((a, b) => new Date(b.date) - new Date(a.date))
                .map((appointment, index) => (
                        <PatientAppointment key={index} updateAppointment={updateAppointment} appointment={appointment}/>
                    )
                );
            setAppointments(appointmentsList);
        }).catch(error => {
            alert(error);
            setAppointments(null);
        });
    }, []);

    function updateAppointment(index, newAppointment) {
        console.log(newAppointment)
        const newAppointments = [...appointments];
        newAppointments[index] = newAppointment;
        setAppointments(newAppointments);
    }

    return (
        <div style={{width: "50%"}}>
            <h3>Appointments for {state.patient.firstName + " " + state.patient.lastName}</h3>
            <p>Email: {state.patient.email} | Phone: {state.patient.phoneNumber}</p>
            {appointments != null ? (appointments.length > 0 ? appointments : "No appointments") : "Loading..."}
        </div>
    )
}

export default PatientAppointments;