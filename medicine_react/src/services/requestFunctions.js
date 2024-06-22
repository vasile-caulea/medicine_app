import {LOGOUT_URL} from "./utils";

async function LogoutFunction(token) {
    const tokenJson = {
        token: token,
    }
    return await fetch(LOGOUT_URL, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(tokenJson),
    });
}


export {LogoutFunction};
