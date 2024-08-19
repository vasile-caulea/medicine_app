package com.pos.medicineConsults.view.hateoas;

import com.pos.medicineConsults.controller.ConsultationController;
import com.pos.medicineConsults.model.POJO.Consultation;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ConsultationHateoas implements RepresentationModelAssembler<Consultation, EntityModel<Consultation>> {
    @NotNull
    @Override
    public EntityModel<Consultation> toModel(@NotNull Consultation entity) {
        EntityModel<Consultation> consultationEntityModel = EntityModel.of(entity);
        Link[] links = {
                linkTo(methodOn(ConsultationController.class).getConsultation(null, entity.getId())).withSelfRel(),
                linkTo(methodOn(ConsultationController.class).getConsultation(null, entity.getIdPatient(), entity.getIdPhysician(), entity.getDate())).withSelfRel(),
                linkTo(methodOn(ConsultationController.class).updateConsultation(null, entity.getId(), null)).withRel("update").withType("PATCH"),
                linkTo(methodOn(ConsultationController.class).addInvestigation(null, entity.getId(), null)).withRel("addInvestigation").withType("PATCH"),
        };

        consultationEntityModel.add(links);

        return consultationEntityModel;
    }
}
