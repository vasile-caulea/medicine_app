import {
    createPhysicianBodyRequest,
    getPatientPatchBody,
    getPhysicianPatchBody,
    PATHS,
    USER_TYPE
} from "../services/utils";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {AddPhysicianService} from "../services/physicianServices";
import {UpdatePhysicianOrPatient} from "../services/services";

function Profile(props) {

    const [userData, setUserData] = useState(null);
    const [userType, setUserType] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        if (props.patientData) {
            setUserData(props.patientData)
            setUserType(USER_TYPE.PATIENT);
        } else {
            setUserData(props.physicianData)
            setUserType(USER_TYPE.PHYSICIAN);
        }
    }, [props.patientData, props.physicianData])


    async function handleSubmit(e) {
        e.preventDefault();
        if (props.userState.setupPhysician) {
            const physicianData = createPhysicianBodyRequest(e.target);
            AddPhysicianService(physicianData, props.userState.id).then(() => {
                props.updateUserState({setupPhysician: false});
                navigate(PATHS.PROFILE);
            }).catch(error => {
                alert(error.message)
            })
        } else {
            let patchBody = null
            let id = null
            if (userType === USER_TYPE.PHYSICIAN) {
                patchBody = getPhysicianPatchBody(e.target)
                id = props.physicianData.idPhysician
            }
            if (userType === USER_TYPE.PATIENT) {
                patchBody = getPatientPatchBody(e.target)
                id = props.patientData.cnp
            }
            try {
                const result = await UpdatePhysicianOrPatient(patchBody, id, userType)
                alert(result.message)
                props.updateUserData({[userType]: {...userData, ...patchBody}})
                navigate(PATHS.PROFILE, {replace: true});
            } catch (error) {
                alert(error.message)
            }
        }
    }

    function updateUserData(e) {
        const {name, value} = e.target;
        setUserData((prevData) => ({...prevData, [name]: value}));
    }

    return (
        <> {props.userState.setupPhysician && (<h1>Setup your doctor profile</h1>)}
            {(userData || props.userState.setupPhysician) ? (<form onSubmit={handleSubmit}>
                <label> First name: <input type="text" name={"firstName"} value={userData?.firstName}
                                           onChange={e => updateUserData(e)}/>
                </label><br/>
                <label> Last name: <input type="text" name={"lastName"} value={userData?.lastName}
                                          onChange={e => updateUserData(e)}/> </label>
                <br/>
                <label> Email: <input type="text" name={"email"} value={userData?.email}
                                      onChange={e => updateUserData(e)}/>
                </label><br/>
                <label> Phone number: <input type="text" name={"phoneNumber"} value={userData?.phoneNumber}
                                             onChange={e => updateUserData(e)}/>
                </label><br/>
                {userData?.birthdate ? <><label> Birth date:
                    <input type="text" name={"birthdate"} value={userData?.birthdate} readOnly/>
                </label> <br/></> : null}
                {(userData?.specialization || props.userState.setupPhysician) ? <><label> Specialization:
                    <input type="text" name={"specialization"} value={userData?.specialization}/>
                </label><br/> </> : null}
                <input type="submit" value={props.userState.setupPhysician ? "Submit" : "Update"}/>
            </form>) : (<p>Loading...</p>)}
        </>
    )
}

export default Profile;