import {useEffect, useState} from "react";
import {USER_TYPE} from "../services/utils";
import {AddConsultation, GetConsultationService, UpdateConsultation} from "../services/consultationServices";
import Investigation from "./Investigation";


function Consultation(props) {

    const [consultationData, setConsultationData] = useState(null);
    const [investigations, setInvestigations] = useState([]);
    const [updateCount, setUpdateCount] = useState(0);
    const [showViewInvestigations, setShowViewInvestigations] = useState(false);
    const [selectedIndex, setSelectedIndex] = useState(0);

    useEffect(() => {
        console.log(props.appointment)
        if (props.appointment) {
            GetConsultationService(props.appointment.patient.cnp, props.appointment.physician.idPhysician, props.appointment.date).then(data => {
                if (data) {
                    setConsultationData(data);
                    setInvestigations(data['investigations'].map((investigation, index) =>
                        <Investigation key={index} consultation={data} investigation={investigation}
                                       updateCount={updateCount} setUpdateCount={setUpdateCount}
                                       userType={props.userType}/>));
                }
            }).catch(() => {})
        }

    }, [props.appointment, updateCount]);

    function updateConsultation() {
        const diagnosis = selectedIndex === 0 ? 'SICK' : 'HEALTHY';
        UpdateConsultation(consultationData.id, {diagnosis: diagnosis}).then(() => {
            setUpdateCount(updateCount + 1);
        }).catch(error => {
            alert(error)
        })
    }

    function addConsultation() {
        const diagnosis = selectedIndex === 0 ? 'SICK' : 'HEALTHY';
        AddConsultation(props.appointment.patient.cnp, props.appointment.physician.idPhysician, props.appointment.date, diagnosis).then(() => {
            setUpdateCount(updateCount + 1);
        }).catch(error => {
            alert(error)
        })
    }

    function toggleInvestigation() {
        setShowViewInvestigations(!showViewInvestigations);
    }

    return (<>
        {consultationData && <p>Diagnosis: {consultationData.diagnosis}</p>}

        {props.userType === USER_TYPE.PHYSICIAN && <>
            <p>Diagnosis: </p>
            <select name={'consultationDiagnosisSelect'} onChange={e => setSelectedIndex(e.target.selectedIndex)}>
                <option>Sick</option>
                <option>Healthy</option>
            </select>
        </>}
        {props.userType === USER_TYPE.PHYSICIAN && !consultationData &&
            <button onClick={addConsultation}>Add consultation</button>}
        {props.userType === USER_TYPE.PHYSICIAN && consultationData &&
            <button onClick={updateConsultation}>Update consultation</button>}

        <>
            {(USER_TYPE.PHYSICIAN === props.userType || investigations.length > 0) &&
                <h3 className={'investigation-title'} onClick={toggleInvestigation}>Investigations</h3>}
            {showViewInvestigations && (<div className={'investigations-data'}>
                {
                    props.userType === USER_TYPE.PHYSICIAN &&
                    <Investigation consultation={consultationData} updateCount={updateCount}
                                   setUpdateCount={setUpdateCount}
                                   userType={props.userType}/>
                }
                {investigations}
            </div>)}
        </>

    </>)
}

export default Consultation;