package com.pos.medicineApp.interfaces.services;

import com.pos.medicineApp.model.Physician;
import com.pos.medicineApp.view.dto.PhysicianPatchDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.Map;

public interface IPhysicianService {

    EntityModel<Physician> addPhysician(Physician physician);

    EntityModel<Physician> findById(Integer id);

    void update(Integer id, PhysicianPatchDTO physicianPatch);


    EntityModel<Physician> findByUserId(Integer userId);

    CollectionModel<EntityModel<Physician>> findAllWithFilters(Map<String, String> filters, Integer page, Integer itemsPerPage);
}
