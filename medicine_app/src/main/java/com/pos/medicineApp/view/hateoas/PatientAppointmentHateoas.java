package com.pos.medicineApp.view.hateoas;

import com.pos.medicineApp.controller.AppointmentController;
import com.pos.medicineApp.model.Appointment;
import jakarta.annotation.Nonnull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PatientAppointmentHateoas implements RepresentationModelAssembler<Appointment, EntityModel<Appointment>> {

    @Nonnull
    @Override
    public EntityModel<Appointment> toModel(@Nonnull Appointment entity) {
        EntityModel<Appointment> result = EntityModel.of(entity);
        result.add(linkTo(methodOn(AppointmentController.class).getPatientAppointments(
                null, entity.getPatient().getCnp())).withRel("parent"));

        // add cancel-appointment link only if appointment status is null
        if (entity.getAppointmentStatus() == null) {
            result.add(linkTo(methodOn(AppointmentController.class).deleteAppointment(
                    null, entity.getPatient().getCnp(), entity.getPhysician().getIdPhysician(),
                    entity.getDate())).withRel("cancel-appointment").withType("DELETE"));
        }
        return result;
    }

    @Override
    public CollectionModel<EntityModel<Appointment>> toCollectionModel(Iterable<? extends Appointment> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
