package com.pos.medicineApp.interfaces.services;

import com.pos.medicineApp.model.Patient;
import com.pos.medicineApp.view.dto.PatientPatchDTO;
import org.springframework.hateoas.EntityModel;

public interface IPatientService {

    EntityModel<Patient> add(Patient patient);

    EntityModel<Patient> findById(String idPatient);

    void update(String idPatient, PatientPatchDTO patientPatchDTO);

    void delete(String idPatient);

    EntityModel<Patient> findByUserId(Integer userId);
}