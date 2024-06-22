import {BASE_URL} from "./utils";

async function AddPatientService(data) {

    const response = await fetch(`${BASE_URL}/api/medical_office/patients`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${localStorage.getItem('token')}`},
        body: JSON.stringify(data),
    })
    const user_data = await response.json();
    switch (response.status) {
        case 201:
            return Promise.resolve({message: 'Patient added successfully.'})
        case 422:
            return Promise.reject({message: 'Invalid patient data.'})
        default:
            return Promise.reject({message: user_data.message})
    }
}

export {AddPatientService}