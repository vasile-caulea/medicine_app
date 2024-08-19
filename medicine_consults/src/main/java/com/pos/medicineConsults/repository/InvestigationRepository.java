package com.pos.medicineConsults.repository;

import com.pos.medicineConsults.model.POJO.Investigation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestigationRepository extends MongoRepository<Investigation, String> {

}
