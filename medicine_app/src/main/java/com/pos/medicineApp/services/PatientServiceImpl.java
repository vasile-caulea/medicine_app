package com.pos.medicineApp.services;

import com.pos.medicineApp.exceptions.ConflictException;
import com.pos.medicineApp.exceptions.NotFoundException;
import com.pos.medicineApp.exceptions.UnprocessableContentException;
import com.pos.medicineApp.interfaces.services.IPatientService;
import com.pos.medicineApp.model.Patient;
import com.pos.medicineApp.repository.PatientRepository;
import com.pos.medicineApp.utils.Utils;
import com.pos.medicineApp.view.dto.PatientPatchDTO;
import com.pos.medicineApp.view.hateoas.PatientHateoas;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;


@Service
class PatientServiceImpl implements IPatientService {
    private final PatientRepository patientRepository;
    private final PatientHateoas patientHateoas;
    private final ModelMapper modelMapper;

    public PatientServiceImpl(PatientRepository patientRepository, PatientHateoas patientHateoas) {
        this.patientRepository = patientRepository;
        this.patientHateoas = patientHateoas;
        this.modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }

    private Patient getActivePatient(String idPatient) {
        Optional<Patient> patient = patientRepository.findByCnpAndIsActive(idPatient, true);
        if (patient.isEmpty())
            throw new NotFoundException("Could not find patient " + idPatient);
        return patient.get();
    }

    @Override
    public EntityModel<Patient> add(Patient patient) {

        if (patient.getCnp() == null)
            throw new UnprocessableContentException("Invalid patient data");

        Optional<Patient> repositoryPatient = patientRepository.findById(patient.getCnp());
        if (repositoryPatient.isPresent()) {
            throw new ConflictException("Patient already exists.");
        }

        Date birthdate;
        try {
            birthdate = Utils.calculateBirthDateFromCNP(patient.getCnp());
        } catch (Exception ex) {
            throw new UnprocessableContentException("Invalid CNP: " + patient.getCnp());
        }

        patient.setIsActive(true);
        patient.setBirthdate(birthdate);
        return patientHateoas.toModel(patientRepository.save(patient));
    }

    @Override
    public EntityModel<Patient> findById(String idPatient) {
        return patientHateoas.toModel(getActivePatient(idPatient));
    }

    @Override
    public void update(String idPatient, PatientPatchDTO patientPatch) {
        if (!patientPatch.isValid())
            throw new UnprocessableContentException("Patient update fields are invalid");
        Patient patient = getActivePatient(idPatient);

        modelMapper.map(patientPatch, patient);
        patientRepository.save(patient);
    }

    @Override
    public void delete(String idPatient) {
        Patient patient = getActivePatient(idPatient);
        patientRepository.deletePatientByCnp(idPatient);
    }

    @Override
    public EntityModel<Patient> findByUserId(Integer userId) {
        Optional<Patient> patient = patientRepository.findByIdUserAndIsActive(userId, true);
        if (patient.isEmpty())
            throw new NotFoundException("Could not find patient " + userId);
        return patientHateoas.toModel(patient.get());
    }
}