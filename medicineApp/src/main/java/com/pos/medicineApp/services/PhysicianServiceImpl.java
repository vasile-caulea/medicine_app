package com.pos.medicineApp.services;

import com.pos.medicineApp.exceptions.NotFoundException;
import com.pos.medicineApp.exceptions.UnprocessableContentException;
import com.pos.medicineApp.interfaces.services.IPhysicianService;
import com.pos.medicineApp.model.Physician;
import com.pos.medicineApp.repository.PhysicianRepository;
import com.pos.medicineApp.view.dto.PhysicianPatchDTO;
import com.pos.medicineApp.view.hateoas.PhysicianHateoas;
import jakarta.persistence.criteria.Predicate;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PhysicianServiceImpl implements IPhysicianService {

    private final PhysicianRepository physicianRepository;

    private final PhysicianHateoas physicianHateoas;

    private final ModelMapper modelMapper;

    public PhysicianServiceImpl(PhysicianRepository physicianRepository, PhysicianHateoas physicianHateoas) {
        this.physicianRepository = physicianRepository;
        this.physicianHateoas = physicianHateoas;
        this.modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }

    @Override
    public EntityModel<Physician> addPhysician(Physician physician) {
        physician.setIdPhysician(null);
        Physician newPhysician = physicianRepository.save(physician);
        return physicianHateoas.toModel(newPhysician);
    }

    @Override
    public EntityModel<Physician> findById(Integer id) {
        Optional<Physician> physician = physicianRepository.findById(id);
        if (physician.isEmpty()) throw new NotFoundException("Could not find physician with id = " + id);
        return physicianHateoas.toModel(physician.get());
    }

    @Override
    public EntityModel<Physician> findByUserId(Integer userId) {
        Optional<Physician> physician = physicianRepository.findByIdUser(userId);
        if (physician.isEmpty()) throw new NotFoundException("Could not find physician with userId = " + userId);
        return physicianHateoas.toModel(physician.get());
    }

    @Override
    public CollectionModel<EntityModel<Physician>> findAllWithFilters(Map<String, String> filters, Integer page, Integer itemsPerPage) {
        Pageable pageable = null;
        if (page != null) {
            pageable = PageRequest.of(page, itemsPerPage);
        }
        Specification<Physician> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filters.containsKey("specialization")) {
                predicates.add(criteriaBuilder.like(root.get("specialization"), "%" + filters.get("specialization") + "%"));
            }

            if (filters.containsKey("lastName")) {
                predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + filters.get("lastName") + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        List<Physician> physicianList;
        if (pageable != null)
            physicianList = physicianRepository.findAll(spec, pageable).stream().toList();
        else
            physicianList = physicianRepository.findAll(spec);
        return physicianHateoas.toCollectionModel(physicianList);
    }

    @Override
    public void update(Integer id, PhysicianPatchDTO physicianPatch) {
        if (!physicianPatch.isValid())
            throw new UnprocessableContentException("Physician update fields are invalid");

        Optional<Physician> physicianR = physicianRepository.findById(id);
        if (physicianR.isEmpty())
            throw new NotFoundException("Could not find physician with id = " + id);

        Physician physician = physicianR.get();
        modelMapper.map(physicianPatch, physician);

        physicianRepository.save(physician);
    }
}
