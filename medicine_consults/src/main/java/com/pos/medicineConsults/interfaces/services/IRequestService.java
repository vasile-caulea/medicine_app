package com.pos.medicineConsults.interfaces.services;

import com.pos.medicineConsults.exceptions.ForbiddenException;
import com.pos.medicineConsults.exceptions.NotFoundException;
import com.pos.medicineConsults.exceptions.UnauthorizedException;
import com.pos.medicineConsults.utils.UserType;

import java.util.Date;

public interface IRequestService {

    /**
     * Retrieves the appointment for a specific user type.
     *
     * @param userType            the type of user (patient or physician)
     * @param authorizationHeader the authorization header for the HTTP request
     * @param patientId           the ID of the patient
     * @param doctorId            the ID of the physician
     * @param date                the date of the appointment
     * @return the appointment details as a string
     * @throws UnauthorizedException if the user is not authorized to access the appointment
     * @throws NotFoundException     if the appointment is not found
     * @throws ForbiddenException    if the user is not allowed to access the appointment
     * @throws InternalError         if an internal error occurs
     */
    String getAppointmentForUserType(UserType userType, String authorizationHeader, String patientId, Integer doctorId, Date date);

}
