package com.pos.medicineApp.repository;

import com.pos.medicineApp.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {

    Optional<Patient> findByCnpAndIsActive(String cnp, Boolean isActive);

    Optional<Patient> findByIdUserAndIsActive(Integer id, Boolean isActive);

    @Modifying
    @Query("UPDATE Patient p SET p.isActive = false WHERE p.cnp = ?1")
    void deletePatientByCnp(String id);

    boolean existsByIdUserAndCnp(Integer id, String patientId);
}
