import React, {useEffect, useState} from "react";
import {GetAppointmentsService, GetMyPatients} from "../services/appointmentsServices";
import MyAppointment from "./MyAppointment";
import {useNavigate} from "react-router-dom";
import {PATHS, USER_TYPE} from "../services/utils";
import Patient from "./Patient";

function Patients(props) {

    const [myPatients, setMyPatients] = useState(null);
    const navigator = useNavigate();

    useEffect(() => {
        if (!props.userState.loggedIn) {
            alert("You must be logged in to view appointments");
            navigator(PATHS.LOGIN);
            return;
        }
        GetMyPatients(props.physicianData.idPhysician).then(data => {
            if (!data['_embedded']['patientList']) {
                setMyPatients([]);
                return;
            }
            const appointmentsList = data['_embedded']['patientList']
                .map((patient, index) => (
                        <Patient key={index} patient={patient} physicianData={props.physicianData}/>
                    )
                );
            setMyPatients(appointmentsList);
        }).catch(error => {
            alert(error);
            setMyPatients(null);
        });
    }, []);

    return (
        <div>
            <h3>My patients</h3>
            <div>
                {myPatients != null ? (myPatients.length > 0 ? myPatients : "No patients") : "Loading..."}
            </div>
        </div>
    );
}

export default Patients;