import React, {useEffect, useState} from "react";
import {PATHS} from "../services/utils";
import {useNavigate} from "react-router-dom";

function Patient(props) {

    const [patientData, setPatientData] = useState(null);
    const navigator = useNavigate();

    useEffect(() => {
        setPatientData(props.patient)
    })

    const openAppointments = () => {
        navigator(PATHS.PATIENT_APPOINTMENTS, {state: {patient: patientData, physician: props.physicianData}});
    }

    return (
        <div className={'patient'}>
            <p>{patientData?.firstName} {patientData?.lastName}</p>
            <p>Email: {patientData?.email} | Phone: {patientData?.phoneNumber}</p>
            <button onClick={openAppointments}>View appointments</button>
        </div>
    );
}

export default Patient;