import {addMinutes, format, isEqual, set} from "date-fns";
import {utcToZonedTime} from "date-fns-tz";

const BASE_URL = "http://localhost:8000";

const MEDICAL_OFFICE_URL = `${BASE_URL}/api/medical_office`;
const LOGOUT_URL = `${BASE_URL}/idm/logout`;
const LOGIN_URL = `${BASE_URL}/idm/tokens`

const USER_TYPE = {
    PHYSICIAN: 0, PATIENT: 1,
}

export {BASE_URL, LOGOUT_URL, LOGIN_URL, MEDICAL_OFFICE_URL, USER_TYPE}

const ROWS_PER_PAGE_OPTIONS = [5, 10, 25, 50]
export {ROWS_PER_PAGE_OPTIONS}

const START_HOUR = 8;
const END_HOUR = 18;
const INTERVAL_MINUTES = 15;
const NUMBER_OF_INTERVALS = (END_HOUR - START_HOUR) * (60 / INTERVAL_MINUTES);
const PATHS = {
    LOGIN: '/login',
    SIGNIN: '/signin',
    SIGNUP: '/signup',
    PROFILE: '/profile',
    MY_APPOINTMENTS: '/my_appointments',
    APPOINTMENTS: '/appointments',
    ADD_DOCTOR: "/add_doctor",
    SETUP: '/setup',
    HOME: '/',
    PATIENT_APPOINTMENTS: "/patient_appointments",
    PHYSICIANS: "/physicians",
    PHYSICIAN: "/physician",
}

export {PATHS}


function getJsonFromForm(target) {
    const data = new FormData(target);
    const jsonData = {};
    data.forEach((value, key) => {
        jsonData[key] = value;
    });
    return jsonData;
}

function createUserBodyRequest(target, role = undefined) {
    const data = new FormData(target);

    let result = {
        'username': data.get('username'), 'password': data.get('password')
    }
    if (role) {
        result['role'] = role
    }
    return result
}

function createBodyObject(target, fields) {
    const data = new FormData(target);
    const body = {};

    fields.forEach(field => {
        body[field] = data.get(field);
    });

    return body;
}

function createPatientBodyRequest(target, userId) {
    const fields = ['cnp', 'lastName', 'firstName', 'email', 'phoneNumber'];
    let body = createBodyObject(target, fields);
    body['idUser'] = userId
    return body
}

function getPatientPatchBody(target) {
    const fields = ['lastName', 'firstName', 'email', 'phoneNumber'];
    return createBodyObject(target, fields);
}


function createPhysicianBodyRequest(target) {
    const fields = ['firstName', 'lastName', 'email', 'phoneNumber', 'specialization'];
    return createBodyObject(target, fields);
}

function getPhysicianPatchBody(target) {
    const fields = ['firstName', 'lastName', 'email', 'phoneNumber'];
    return createBodyObject(target, fields);
}


function setProps(props, token, username) {
    try {
        let base64Payload = token.split(".")[1];
        let payload = atob(base64Payload);
        payload = JSON.parse(payload.toString());

        const newState = {
            loggedIn: true, username: username, id: payload['sub'], roles: payload['roles']
        }
        props.updateUserState(newState);
        localStorage.setItem('token', token);
    } catch (error) {
        console.log(error);
    }
}


function getTypeAsString(type) {
    // eslint-disable-next-line default-case
    switch (type) {
        case USER_TYPE.PHYSICIAN:
            return 'Physician';
        case USER_TYPE.PATIENT:
            return 'Patient';
    }
}
function filterObject(obj) {
    const keys = Object.keys(obj);
    const validKeys = keys.filter(key => obj[key] !== null && obj[key] !== undefined && obj[key] !== '');
    return validKeys.reduce((acc, key) => {
        acc[key] = obj[key];
        return acc;
    }, {});
}
function generateHours(dates) {
    dates = dates.map(date => {
        return utcToZonedTime(date, 'Europe/Bucharest');
    })
    let currentDateTime = dates[0];
    const hourList = [];
    currentDateTime = set(currentDateTime, {
        hours: START_HOUR,
        minutes: 0,
        seconds: 0
    });
    for (let i = 0; i < NUMBER_OF_INTERVALS; i++) {
        if (!dates.some(date => isEqual(date, currentDateTime))) {
            hourList.push(format(currentDateTime, 'HH:mm'));
        }
        currentDateTime = addMinutes(currentDateTime, INTERVAL_MINUTES);
    }
    return hourList;
}


export {
    getJsonFromForm,
    createPatientBodyRequest,
    createUserBodyRequest,
    createPhysicianBodyRequest,
    setProps,
    getPhysicianPatchBody,
    getTypeAsString,
    getPatientPatchBody,
    generateHours,
    filterObject
}