import React, {useEffect, useState} from "react";
import {GetMyAppointmentsService} from "../services/appointmentsServices";
import MyAppointment from "./MyAppointment";
import {useNavigate} from "react-router-dom";
import {PATHS} from "../services/utils";

function MyAppointments(props) {

    const [appointments, setAppointments] = useState(null);
    const navigator = useNavigate();

    useEffect(() => {
        if (!props.userState.loggedIn) {
            alert("You must be logged in to view appointments");
            navigator(PATHS.LOGIN);
            return;
        }
        GetMyAppointmentsService(props.patientData.cnp).then(data => {
            const embedded = data['_embedded'];
            if (!embedded || !embedded['appointmentList']) {
                setAppointments([]);
                return;
            }
            const appointmentsList = data['_embedded']['appointmentList']
                .sort((a, b) => new Date(b.date) - new Date(a.date))
                .map((appointment, index) => (
                        <MyAppointment key={index} appointment={appointment}/>
                    )
                );
            setAppointments(appointmentsList);
        }).catch(error => {
            alert(error);
            setAppointments(null);
        });
    }, []);

    return (
        <div className={"my-appointments"}>
            <h3>My appointments</h3>
            <div>
                {appointments != null ? (appointments.length > 0 ? appointments : "No appointments") : "Loading..."}
            </div>
        </div>
    );
}

export default MyAppointments;