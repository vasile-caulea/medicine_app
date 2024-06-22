import {useEffect, useState} from "react";
import {AddInvestigation, UpdateInvestigation} from "../services/consultationServices";
import {filterObject, USER_TYPE} from "../services/utils";

function Investigation(props) {
    const [name, setName] = useState("");
    const [duration, setDuration] = useState(1);
    const [result, setResult] = useState("");
    const [buttonText, setButtonText] = useState("Add");

    const [wasSet, setWasSet] = useState(false);

    useEffect(() => {
        if (props.investigation && !wasSet) {
            setName(props.investigation.name);
            setDuration(props.investigation.duration);
            setResult(props.investigation.result);
            setButtonText("Update");
            setWasSet(true);
        }
    }, [props.investigation]);

    function handleInvestigationOperation(e, operation) {
        e.preventDefault();

        if (operation === 'add') {
            AddInvestigation(props.consultation.id, name, duration, result).then(() => {
                props.setUpdateCount(props.updateCount + 1);
            }).catch(error => {
                alert(error)
            })
        } else if (operation === 'update') {
            let body = {}
            if (props.investigation.name !== name) {
                body['name'] = name
            }
            if (props.investigation.duration !== duration) {
                body['duration'] = duration
            }
            if (props.investigation.result !== result) {
                body['result'] = result
            }
            body = filterObject(body)
            if (Object.keys(body).length === 0) {
                alert("Nothing to update");
                return;
            }

            UpdateInvestigation(props.consultation.id, props.investigation.id, body)
                .then(data => {
                    alert(data.message);
                })
                .catch(error => {
                    alert(error);
                });
        }
    }

    return (<>
        {(props.userType === USER_TYPE.PHYSICIAN) ?
            <form className={'investigation_form'} name={'investigation_form'}>
                <label>Name:</label> <input name='name' type="text" value={name}
                                            onChange={(e) => setName(e.target.value)}/><br/>
                <label>Duration in days:</label> <input name='duration' type="number" min={1} value={duration}
                                                        onChange={(e) => setDuration(e.target.value)}/><br/>
                <label>Result:</label><br/><textarea name='result' value={result}
                                                     onChange={(e) => setResult(e.target.value)}></textarea>
                {<input type="submit" value={buttonText}
                        onClick={(e) => handleInvestigationOperation(e, buttonText.toLowerCase())}/>}

            </form> : <div className={'investigation_view'}>
                <p>Name: {name}</p>
                <p>Duration: {duration} days</p>
                <p>Result: {result}</p>
            </div>}
    </>);
}

export default Investigation;
