package com.pos.medicineApp.view.hateoas;

import com.pos.medicineApp.controller.PatientController;
import com.pos.medicineApp.model.Patient;
import jakarta.annotation.Nonnull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// https://codejava.net/frameworks/spring-boot/rest-api-crud-with-hateoas-tutorial
@Component
public class PatientHateoas implements RepresentationModelAssembler<Patient, EntityModel<Patient>> {
    @Nonnull
    @Override
    public EntityModel<Patient> toModel(@Nonnull Patient patient) {
        EntityModel<Patient> patientEntityModel = EntityModel.of(patient);

        patientEntityModel.add(linkTo(methodOn(PatientController.class).getPatient(null, patient.getCnp())).withSelfRel());
        return patientEntityModel;
    }
}
