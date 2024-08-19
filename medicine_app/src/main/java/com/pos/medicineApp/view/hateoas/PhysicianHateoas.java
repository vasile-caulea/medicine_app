package com.pos.medicineApp.view.hateoas;

import com.pos.medicineApp.controller.PhysicianController;
import com.pos.medicineApp.model.Physician;
import jakarta.annotation.Nonnull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PhysicianHateoas implements RepresentationModelAssembler<Physician, EntityModel<Physician>> {
    @Nonnull
    @Override
    public EntityModel<Physician> toModel(@Nonnull Physician physician) {
        EntityModel<Physician> physicianEntityModel = EntityModel.of(physician);

        physicianEntityModel.add(linkTo(methodOn(PhysicianController.class).getPhysician(physician.getIdPhysician())).withSelfRel());
        physicianEntityModel.add(linkTo(methodOn(PhysicianController.class).getPhysicians( null, null, null)).withRel("parent"));

        return physicianEntityModel;
    }

    @Nonnull
    @Override
    public CollectionModel<EntityModel<Physician>> toCollectionModel(@Nonnull Iterable<? extends Physician> entities) {
        CollectionModel<EntityModel<Physician>> collectionModel = RepresentationModelAssembler.super.toCollectionModel(entities);

        collectionModel.add(linkTo(methodOn(PhysicianController.class).getPhysicians(null, null, null)).withRel("parent"));
        return collectionModel;
    }
}
