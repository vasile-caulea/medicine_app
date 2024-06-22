package com.pos.medicineApp.repository;

import com.pos.medicineApp.model.Physician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PhysicianRepository extends JpaRepository<Physician, Integer>, JpaSpecificationExecutor<Physician> {
    List<Physician> findPhysiciansBySpecializationContainingIgnoreCase(String specialization);

    List<Physician> findPhysiciansByLastNameContainingIgnoreCase(String name);

    boolean existsByIdUserAndIdPhysician(Integer id, Integer idp);

    Optional<Physician> findByIdUser(Integer idUser);
}
