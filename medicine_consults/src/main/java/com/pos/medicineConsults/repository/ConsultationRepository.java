package com.pos.medicineConsults.repository;

import com.pos.medicineConsults.model.POJO.Consultation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationRepository extends MongoRepository<Consultation, String> {
    boolean existsByIdPatientAndIdPhysicianAndDate(String idPatient, Integer idPhysician, Date date);

    List<Consultation> findAllByIdPatient(String idPatient);

    List<Consultation> findAllByIdPhysician(Integer idPhysician);

    Optional<Consultation> findByIdPatientAndIdPhysicianAndDate(String idPatient, Integer idPhysician, Date date);
}
