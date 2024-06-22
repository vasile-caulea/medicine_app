package com.pos.medicineApp.interfaces.services;

import java.util.Map;

public interface IAuthorizingService {

    void authorizePatient(Map<String, String> headers, String patientId);

    void authorizePhysician(Map<String, String> headers, Integer idp);

    void authorizePhysicianByUserId(Map<String, String> headers, Integer iduser);

    void authorizeCreatePhysician(Map<String, String> headers, Integer idUser);

    void authorizeCreatePatient(Map<String, String> headers, Integer idUser);

    void authorizePatientByUserId(Map<String, String> headers, Integer userId);
}
