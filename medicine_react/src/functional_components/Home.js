import {useNavigate} from "react-router-dom";
import {PATHS, USER_TYPE} from "../services/utils";
import {useEffect} from "react";

function Home(props) {

    const navigator = useNavigate()
    const loggedIn = props.userState.loggedIn;
    const roles = props.userState.roles;
    let helloUser = props.userState.username
    if (props.userData[USER_TYPE.PATIENT]) {
        helloUser = props.userData[USER_TYPE.PATIENT]['firstName'] + " " + props.userData[USER_TYPE.PATIENT]['lastName']
    } else if (props.userData[USER_TYPE.PHYSICIAN]) {
        helloUser = props.userData[USER_TYPE.PHYSICIAN]['firstName'] + " " + props.userData[USER_TYPE.PHYSICIAN]['lastName']
    }

    let addDoctorButton = null
    let loginButton = null
    let signInButton = null
    let viewProfileButton = null
    let viewMyAppointmentsButton = null
    let viewPatientsButton = null
    let logoutButton = null
    let viewPhysiciansButton = null
    if (!loggedIn) {
        loginButton = (<button onClick={() => navigator(PATHS.LOGIN)}>Login</button>)
        signInButton = (<button onClick={() => navigator(PATHS.SIGNIN)}>Sign In</button>)
    } else {
        logoutButton = (<button onClick={e => props.logoutClick(e)}>Logout</button>)
        if (roles.includes("admin")) {
            addDoctorButton = (<button onClick={() => navigator(PATHS.ADD_DOCTOR)}>Add Doctor</button>)
        } else {
            viewProfileButton = (<button onClick={() => navigator(PATHS.PROFILE)}>View Profile</button>)
        }
        if (roles.includes("patient")) {
            viewMyAppointmentsButton = (
                <button onClick={() => navigator(PATHS.MY_APPOINTMENTS)}>My appointments</button>)
        }
        if (roles.includes("physician")) {
            viewPatientsButton = (<button onClick={() => navigator(PATHS.APPOINTMENTS)}>My patients</button>)
        }
    }
    if (roles.includes('physician') && !roles.includes('patient')) {
        signInButton = (<button onClick={() => navigator(PATHS.SIGNIN)}>Sign In</button>)
    }
    if (!roles.includes('admin')) {
        viewPhysiciansButton = <button onClick={() => navigator(PATHS.PHYSICIANS)}>View all doctors</button>
    }

    return (<div>
        <h1>Home</h1>
        {loggedIn && <p>Hello {helloUser}</p>}
        {loginButton}
        {signInButton}
        {logoutButton}
        {addDoctorButton}
        {viewProfileButton}
        {viewMyAppointmentsButton}
        {viewPatientsButton}
        {viewPhysiciansButton}
    </div>);
}


export default Home;

