import {CreateUserService, DeleteUser, LoginService, UpdateUserService} from "../services/userServices";
import {createPatientBodyRequest, createUserBodyRequest, PATHS} from "../services/utils";
import {AddPatientService} from "../services/patientServices";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";


function SignIn(props) {
    const navigator = useNavigate()

    const roles = props.userState.roles

    useEffect(() => {
        if (props.physicianData) {
            const form = document.forms['sigin_form']
            form['lastName'].value = props.physicianData['lastName']
            form['firstName'].value = props.physicianData['firstName']
            form['email'].value = props.physicianData.email
            form['phoneNumber'].value = props.physicianData['phoneNumber']
        }

    }, []);

    const handleSignInSubmit = async (e) => {
        e.preventDefault();

        if (roles.includes('physician')) {
            try {
                await UpdateUserService(props.userState.id, {action_role: 'add', role: 'patient'})
            } catch (error) {
                alert(error.message)
                return
            }
            try {
                await AddPatientService(createPatientBodyRequest(e.target, props.userState.id))
                alert('Sign in successful... Redirecting to home page')
                navigator(PATHS.HOME)
            } catch (error) {
                alert(error.message)
                try {
                    await UpdateUserService(props.userState.id, {action_role: 'remove', role: 'patient'})
                } catch (error) {
                    console.log(error.message)
                }
            }
            return
        }

        const userData = createUserBodyRequest(e.target, 'patient')
        let userInfo = undefined
        try {
            userInfo = await CreateUserService(userData)
            await LoginService(props, userData.username, userData.password);
            await AddPatientService(createPatientBodyRequest(e.target, userInfo.message))
            alert('Sign in successful... Redirecting to home page')
            navigator(PATHS.HOME)
        } catch (error) {
            alert(error.message)
            if (userInfo) {
                await DeleteUser(userInfo.message)
            }
        }
    }

    return (
        <>
            <h1>Sign In</h1>
            <p>Create your patient profile</p>
            <form name={'sigin_form'} onSubmit={handleSignInSubmit}>
                {!roles.includes('physician') && <>
                    <label> Username: <input type="text" name="username" required/> </label> <br/>
                    <label> Password: <input type="password" name="password" required/> </label>
                    <br/> </>
                }
                <label> CNP: <input type="text" name="cnp" required/> </label> <br/>
                <label> Last Name: <input type="text" name="lastName" required/> </label> <br/>
                <label> First Name: <input type="text" name="firstName" required/> </label> <br/>
                <label> Email: <input type="email" name="email" required/> </label> <br/>
                <label> Phone Number: <input type="text" name="phoneNumber" required/> </label>
                <br/>
                <input type="submit" value="Sign in"/>
            </form>
        </>
    );
}

export default SignIn;