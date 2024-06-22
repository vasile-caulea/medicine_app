import {BASE_URL, getTypeAsString, USER_TYPE} from "./utils";

async function GetPhysicianOrPatientService(idUser, type) {
    let url = BASE_URL
    if (type === USER_TYPE.PHYSICIAN) {
        url += `/api/medical_office/physicians/user/${idUser}`
    } else {
        url += `/api/medical_office/patients/user/${idUser}`
    }
    return await fetch(url, {
        method: 'GET',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${localStorage.getItem('token')}`},
    })
}

async function UpdatePhysicianOrPatient(body, id, type) {
    let url = BASE_URL
    if (type === USER_TYPE.PHYSICIAN) {
        url += `/api/medical_office/physicians/${id}`
    } else {
        url += `/api/medical_office/patients/${id}`
    }

    const response = await fetch(url, {
        method: 'PATCH',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${localStorage.getItem('token')}`},
        body: JSON.stringify(body),
    })
    if (response.status === 204) {
        return Promise.resolve({message: `${getTypeAsString(type)} updated successfully.`})
    }
    const user_data = await response.json();
    switch (response.status) {
        case 422:
            return Promise.reject({message: 'Invalid update data'})
        default:
            return Promise.reject({message: user_data.message})
    }
}

export {GetPhysicianOrPatientService, UpdatePhysicianOrPatient};