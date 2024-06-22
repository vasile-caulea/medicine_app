import {BASE_URL, MEDICAL_OFFICE_URL, USER_TYPE} from "./utils";

async function GetMyPatients(id) {
    const response = await fetch(`${BASE_URL}/api/medical_office/physicians/${id}/patients?patient=true`, {
        method: 'GET',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${localStorage.getItem('token')}`},
    })
    const patients = await response.json();
    switch (response.status) {
        case 200:
            return Promise.resolve(patients)
        default:
            return Promise.reject(patients.message)
    }
}

async function GetMyAppointmentsService(id) {
    const response = await fetch(`${BASE_URL}/api/medical_office/patients/${id}/physicians`, {
        method: 'GET',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${localStorage.getItem('token')}`},
    })
    const appointment_data = await response.json();
    switch (response.status) {
        case 200:
            return Promise.resolve(appointment_data)
        default:
            return Promise.reject(appointment_data.message)
    }
}

async function UpdateAppointmentStatus(idPatient, idPhysician, date, status) {
    let url = BASE_URL
    const request = {
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${localStorage.getItem('token')}`},
    }

    if (status === 'cancel') {
        url += `/api/medical_office/patients/${idPatient}/physicians/${idPhysician}/${date}`
        request.method = 'DELETE'
    } else {
        url += `/api/medical_office/physicians/${idPhysician}/patients/${idPatient}/${date}`
        request.method = 'PATCH'
        request.body = JSON.stringify({'status': status})
    }

    const response = await fetch(url, request)
    if (response.ok) {
        return Promise.resolve({message: 'Appointment updated successfully.'})
    } else {
        const appointment_data = await response.json()
        return Promise.reject(appointment_data.message)
    }
}

const GetAppointmentById = async (userType, idPatient, idPhysician, date) => {
    let url
    if (userType === USER_TYPE.PHYSICIAN)
        url = `${BASE_URL}/api/medical_office/physicians/${idPhysician}/patients/${idPatient}/${date}`
    else
        url = `${BASE_URL}/api/medical_office/patients/${idPatient}/physicians/${idPhysician}/${date}`
    const response = await fetch(url, {
        method: 'GET',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${localStorage.getItem('token')}`},
    })
    const appointment_data = await response.json();
    switch (response.status) {
        case 200:
            return Promise.resolve(appointment_data)
        default:
            return Promise.reject(appointment_data.message)
    }
}

async function GetMyPatientAppointments(idPatient, idPhysician) {
    const response = await fetch(`${BASE_URL}/api/medical_office/physicians/${idPhysician}/patients/${idPatient}`, {
        method: 'GET',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${localStorage.getItem('token')}`},
    })
    const appointment_data = await response.json();
    switch (response.status) {
        case 200:
            return Promise.resolve(appointment_data)
        default:
            return Promise.reject(appointment_data.message)
    }
}

async function GetAppointmentDates(idPhysician, date) {
    const response = await fetch(`${MEDICAL_OFFICE_URL}/physicians/${idPhysician}/patients/dates?date=${date}`, {
        method: 'GET',
        headers: {'Content-Type': 'application/json'},
    })
    const appointment_data = await response.json();
    switch (response.status) {
        case 200:
            return Promise.resolve(appointment_data)
        default:
            return Promise.reject(appointment_data.message)
    }
}

const AddAppointment = async (idPatient, idPhysician, date, hour) => {
    date = date.format('YYYY-MM-DD') + " " + hour
    const response = await fetch(`${BASE_URL}/api/medical_office/patients/${idPatient}/physicians/${idPhysician}`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${localStorage.getItem('token')}`},
        body: JSON.stringify({'date': date})
    })
    const appointment_data = await response.json();
    switch (response.status) {
        case 200:
            return Promise.resolve(appointment_data)
        default:
            return Promise.reject(appointment_data.message)
    }
}

export {
    GetMyPatients,
    UpdateAppointmentStatus,
    GetMyAppointmentsService,
    GetMyPatientAppointments,
    GetAppointmentById,
    GetAppointmentDates,
    AddAppointment
}