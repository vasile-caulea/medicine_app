package com.pos.medicineApp.view.hateoas;

import com.pos.medicineApp.controller.AppointmentController;
import com.pos.medicineApp.model.Appointment;
import com.pos.medicineApp.model.AppointmentStatus;
import jakarta.annotation.Nonnull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@SuppressWarnings("DataFlowIssue") // for @Nonnull that is used in linkTo
@Component
public class PhysicianAppointmentHateoas implements RepresentationModelAssembler<Appointment, EntityModel<Appointment>> {
    @Nonnull
    @Override
    public EntityModel<Appointment> toModel(@Nonnull Appointment entity) {
        Link link = Link.of("http://127.0.0.1:8081/medical_office/consulations");
        EntityModel<Appointment> result = EntityModel.of(entity);

        // add parent link
        result.add(linkTo(methodOn(AppointmentController.class).getPhysicianAppointmentsOrPatients(null,
                entity.getPhysician().getIdPhysician(),
                null)).withRel("parent"));

        // add add-consultation link only if appointment status is attended
        if (entity.getAppointmentStatus() == AppointmentStatus.ATTENDED) {
            result.add(link.withRel("add-consultation").withType("POST"));
        }

        // add update-appointment-status link only if appointment status is null
        if (entity.getAppointmentStatus() == null) {
            result.add(linkTo(methodOn(AppointmentController.class).updateAppointmentStatus(null,
                    entity.getPatient().getCnp(), entity.getPhysician().getIdPhysician(), entity.getDate(),
                    null)).withRel("update-appointment-status").withType("PATCH"));
        }
        return result;
    }
}
