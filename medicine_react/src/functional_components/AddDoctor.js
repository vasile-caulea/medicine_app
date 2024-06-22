import {CreateUserService} from "../services/userServices";
import {createUserBodyRequest} from "../services/utils";

export function AddDoctor(props) {

    const handleAddDoctorSubmit = async (e) => {
        e.preventDefault();
        const userData = createUserBodyRequest(e.target, 'physician');
        try {
            await CreateUserService(userData, localStorage.getItem('token'));
            alert("User created")
        } catch (error) {
            alert(error.message)
        }
    }

    return (
        <>
            <h2>Add doctor user</h2>
            <form onSubmit={handleAddDoctorSubmit}>
                <label> Username: <input type="text" name="username" required/> </label> <br/>
                <label> Password: <input type="password" name="password" required/> </label> <br/>
                <input type="submit" value="Add"/>
            </form>
        </>
    )
}