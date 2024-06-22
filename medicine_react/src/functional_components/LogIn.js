import {useNavigate} from 'react-router-dom';
import {LoginService} from "../services/userServices";

function LogIn(props) {

    const navigate = useNavigate();
    const handleLoginSubmit = async (e) => {
        e.preventDefault();

        const username = e.target.username.value
        const password = e.target.password.value
        try {
            await LoginService(props, username, password);
            navigate("/")
        } catch (error) {
            alert(error.message)
        }
    }

    return (
        <form onSubmit={handleLoginSubmit}>
            <label> Username: <input type="text" name="username" required/> </label> <br/>
            <label> Password: <input type="password" name="password" required/> </label> <br/>
            <input type="submit" value="Log in"/>
        </form>
    );
}

export default LogIn;