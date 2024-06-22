import {BASE_URL, LOGIN_URL, setProps} from "./utils";


async function LoginService(props, username, password) {
    const loginData = {
        username: username,
        password: password,
    }

    const response = await fetch(LOGIN_URL, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(loginData),
    })

    const data = await response.json();
    if (response.status === 201) {
        setProps(props, data.message, username);
        return Promise.resolve({message: 'LogIn successful'});
    } else {
        return Promise.reject({message: 'LogIn failed. ' + data.message});
    }
}

async function CreateUserService(userdata, token = null) {

    let headers = {'Content-Type': 'application/json'}
    if (token) {
        headers['Authorization'] = `Bearer ${token}`
    }

    const response = await fetch(`${BASE_URL}/idm/users`, {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(userdata),
    });
    const data = await response.json();
    switch (response.status) {
        case 201:
            return Promise.resolve({message: data.message});
        case 409:
            return Promise.reject({message: 'User already exists.'});
        default:
            return Promise.reject({message: data.message});
    }
}

async function DeleteUser(id) {
    const response = await fetch(`${BASE_URL}/idm/users/${id}`, {
        method: 'DELETE',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${localStorage.getItem('token')}`},
    });
    const data = await response.json();
    console.log(data)
}

async function UpdateUserService(id, body) {
    const response = await fetch(`${BASE_URL}/idm/users/${id}`, {
        method: 'PATCH',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${localStorage.getItem('token')}`},
        body: JSON.stringify(body),
    });
    if (response.ok)
        return Promise.resolve(true)
    const data = await response.json();
    return Promise.reject({message: data.message});
}

export {LoginService, CreateUserService, DeleteUser, UpdateUserService};