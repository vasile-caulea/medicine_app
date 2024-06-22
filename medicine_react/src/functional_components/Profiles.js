import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import Profile from "./Profile";
import {PATHS, USER_TYPE} from "../services/utils";
import {UpdateUserService} from "../services/userServices";


function Profiles({userData, updateUserData, userState, updateUserState}) {
    const navigator = useNavigate();
    const [username, setUsername] = useState(null)
    const [password, setPassword] = useState(null)

    useEffect(() => {
        if (!userState.loggedIn) {
            alert("You must be logged in to view your profile");
            navigator(PATHS.LOGIN)
        }
        document.forms['user-profile-form']['username'].value = userState.username
    }, [userData, userState]);

    function updateUserProfile(e) {
        e.preventDefault()
        const body = {}
        if (username) body.username = username
        if (password) body.password = password
        if (Object.keys(body).length === 0) {
            alert("No changes were made")
            return
        }
        UpdateUserService(userState.id, body).then(r => {
            alert('Profile updated')
            updateUserState({username: e.target.username.value})
        }).catch(error => {
            alert(error.message)
        })
    }

    return (<div>
        <div style={{padding: "20px"}}>
            <h2>My user profile</h2>
            <form name="user-profile-form" className="user-profile-form" onSubmit={updateUserProfile}>
                <div><label>Username: </label><input type="text" name="username"
                                                     onChange={e => setUsername(e.target.value)}/></div>
                <div><label>Password: </label><input type="password" name="password"
                                                     onChange={e => setPassword(e.target.value)}/></div>
                <input type="submit" value="Update"/>
            </form>
        </div>
        {userData[USER_TYPE.PATIENT] && <div>
            <h2>My patient profile</h2>
            <Profile userState={userState} updateUserData={updateUserData} patientData={userData[USER_TYPE.PATIENT]}/>
        </div>}
        {userData[USER_TYPE.PHYSICIAN] && (<div>
            <h2>My physician profile</h2>
            <Profile userState={userState} updateUserData={updateUserData}
                     physicianData={userData[USER_TYPE.PHYSICIAN]}/>
        </div>)}
    </div>)
}

export default Profiles;