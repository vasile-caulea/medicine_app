import '../style/style.css';
import LogIn from './LogIn';
import {useEffect, useState} from "react";
import {Route, Routes, useNavigate} from 'react-router-dom'
import Home from './Home';
import SignIn from "./SignIn";
import {AddDoctor} from "./AddDoctor";
import Profiles from "./Profiles";
import Patients from "./Patients";
import {GetPhysicianOrPatientService} from "../services/services";
import {PATHS, USER_TYPE} from "../services/utils";
import Profile from "./Profile";
import {LogoutFunction} from "../services/requestFunctions";
import MyAppointments from "./MyAppointments";
import PatientAppointments from "./PatientAppointments";
import Physicians from "./Physicians";
import PhysicianDetails from "./PhysicianDetails";


function App() {

    const navigate = useNavigate()

    const [userState, setUserState] = useState({
        loggedIn: false,
        username: '',
        id: '',
        roles: [],
        setupPhysician: false
    });

    const [userData, setUserData] = useState({
        [USER_TYPE.PATIENT]: null,
        [USER_TYPE.PHYSICIAN]: null
    });

    function updateUserState(newState) {
        setUserState({...userState, ...newState});
    }

    function updateUserData(newData) {
        setUserData({...userData, ...newData});
    }

    useEffect(() => {
        const getUserProfile = async (role, userType) => {
            if (userState.roles.includes(role)) {
                GetPhysicianOrPatientService(userState.id, userType).then(async response => {
                    const body = await response.json();
                    if (response.ok) {
                        userData[userType] = body;
                        setUserData(userData);
                    } else if (response.status === 404 && userType === USER_TYPE.PHYSICIAN) {
                        updateUserState({setupPhysician: true});
                        console.log(`${userType} profile not set up yet`);
                        navigate(PATHS.SETUP);
                    } else {
                        console.log(response.status)
                        console.log(body.message);
                    }
                }).catch(error => {
                    alert(error);
                });
            } else {
                userData[userType] = null;
                setUserData(userData);
            }
        }
        for (let role of userState.roles) {
            if (role === 'patient') {
                getUserProfile(role, USER_TYPE.PATIENT).then();
            }
            if (role === 'physician') {
                getUserProfile(role, USER_TYPE.PHYSICIAN).then();
            }
        }
    }, [userState]);

    async function logoutClick(e) {
        e.preventDefault()
        if (!userState.loggedIn) {
            alert("You must be logged in to log out");
            return;
        }
        const response = await LogoutFunction(localStorage.getItem('token'));
        if (response.ok) {
            updateUserState({
                loggedIn: false,
                username: '',
                id: '',
                roles: [],
            });
            updateUserData({[USER_TYPE.PATIENT]: null, [USER_TYPE.PHYSICIAN]: null})
            localStorage.removeItem("token");
            alert("Logout successful.. Redirecting to LogIn");
            navigate(PATHS.LOGIN);
        }
    }

    return (
        <div className={"app"}>
            <Routes>
                <Route path={PATHS.HOME} element={<Home logoutClick={logoutClick} userState={userState}
                                                        updateUserState={updateUserState}
                                                        userData={userData}/>}/>
                <Route path={PATHS.LOGIN} element={<LogIn updateUserState={updateUserState}/>}/>
                <Route path={PATHS.SIGNIN}
                       element={<SignIn userState={userState} physicianData={userData[USER_TYPE.PHYSICIAN]}
                                        updateUserState={updateUserState}/>}/>
                <Route path={PATHS.ADD_DOCTOR} element={<AddDoctor updateUserState={updateUserState}/>}/>
                <Route path={PATHS.PROFILE} element={<Profiles userData={userData} updateUserData={updateUserData}
                                                               userState={userState}
                                                               updateUserState={updateUserState}/>}/>
                <Route path={PATHS.MY_APPOINTMENTS}
                       element={<MyAppointments patientData={userData[USER_TYPE.PATIENT]} userState={userState}
                                                updateUserState={updateUserState}/>}/>
                <Route path={PATHS.APPOINTMENTS}
                       element={<Patients physicianData={userData[USER_TYPE.PHYSICIAN]} userState={userState}/>}/>
                <Route path={PATHS.SETUP} element={<Profile userState={userState} updateUserState={updateUserState}/>}/>
                <Route path={PATHS.PATIENT_APPOINTMENTS} element={<PatientAppointments/>}/>
                <Route path={PATHS.PHYSICIANS} element={<Physicians/>}/>
                <Route path={PATHS.PHYSICIAN}
                       element={<PhysicianDetails patient={userData[USER_TYPE.PATIENT]} userState={userState}/>}/>
            </Routes>
        </div>
    );
}

export default App;
