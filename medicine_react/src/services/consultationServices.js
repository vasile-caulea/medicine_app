import {BASE_URL} from "./utils";


async function GetConsultationService(id_patient, id_physician, date) {

    const response = await fetch(`${BASE_URL}/api/medical_office/consultations/${id_patient}/${id_physician}/${date}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('token'),
        },
    });
    const data = await response.json();
    if (response.ok) {
        return Promise.resolve(data);
    } else if (response.status === 404) {
        console.log(data.message)
    } else {
        return Promise.reject(data.message);
    }
}

const UpdateConsultation = async (id, patchBody) => {

    const response = await fetch(`${BASE_URL}/api/medical_office/consultations/${id}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('token'),
        },
        body: JSON.stringify(patchBody),
    });

    if (response.ok) {
        return Promise.resolve({message: 'Consultation updated successfully.'});
    } else {
        const er = await response.json();
        return Promise.reject({message: er.message});
    }
}

async function AddConsultation(patientId, physicianId, date, diagnosis) {

    const response = await fetch(`${BASE_URL}/api/medical_office/consultations`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('token'),
        },
        body: JSON.stringify({
            idPatient: patientId,
            idPhysician: physicianId,
            date: date,
            diagnosis: diagnosis
        }),
    });

    if (response.ok) {
        return Promise.resolve({message: 'Consultation added successfully.'});
    } else {
        const er = await response.json();
        return Promise.reject({message: er.message});
    }
}

async function AddInvestigation(id_consultation, name, duration, result) {

    const response = await fetch(`${BASE_URL}/api/medical_office/consultations/${id_consultation}/investigations`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('token'),
        },
        body: JSON.stringify({
            name: name,
            duration: duration,
            result: result
        }),
    });

    const data = await response.json();
    if (response.ok) {
        return Promise.resolve(data);
    } else {
        return Promise.reject(data.message);
    }

}

const UpdateInvestigation = async (idConsultation, idInvestigation, patchBody) => {

    const response = await fetch(`${BASE_URL}/api/medical_office/consultations/${idConsultation}/investigations/${idInvestigation}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('token'),
        },
        body: JSON.stringify(patchBody),
    });

    if (response.ok) {
        return Promise.resolve({message: 'Investigation updated successfully.'});
    }
    const data = await response.json();
    return Promise.reject(data.message);
}

export {GetConsultationService, UpdateConsultation, AddConsultation, AddInvestigation, UpdateInvestigation};

