import {BASE_URL, filterObject} from "./utils";
import {createSearchParams} from "react-router-dom";

async function AddPhysicianService(physicianBody, userId) {
    physicianBody['idUser'] = userId;

    const response = await fetch(`${BASE_URL}/api/medical_office/physicians`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${localStorage.getItem('token')}`},
        body: JSON.stringify(physicianBody),
    })
    const user_data = await response.json();
    switch (response.status) {
        case 201:
            return Promise.resolve({message: 'Physician added successfully.'})
        default:
            return Promise.reject({message: user_data.message})
    }
}


async function GetPhysicians(queryParams) {
    console.log(queryParams)
    const params = createSearchParams(filterObject(queryParams))
    console.log(params.toString())
    const response = await fetch(`${BASE_URL}/api/medical_office/physicians?${params.toString()}`, {
        method: 'GET',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${localStorage.getItem('token')}`},
    })
    const user_data = await response.json();
    switch (response.status) {
        case 200:
            return Promise.resolve(user_data)
        default:
            return Promise.reject({message: user_data.message})
    }
}

export {AddPhysicianService, GetPhysicians}