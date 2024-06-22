package com.pos.medicineConsults.interfaces.services;

import com.pos.medicineConsults.model.ConsultationPatchDTO;
import com.pos.medicineConsults.model.POJO.Consultation;
import com.pos.medicineConsults.model.POJO.Investigation;
import org.springframework.hateoas.EntityModel;

import java.util.Date;
import java.util.Map;

public interface IConsultationService {

    EntityModel<Consultation> addConsultation(Map<String, String> headers, Consultation consultation);

    EntityModel<Consultation> addInvestigation(Map<String, String> headers, String id, Investigation investigation);

    EntityModel<Consultation> getConsultation(Map<String, String> headers, String idConsult);

    EntityModel<Consultation> getConsultation(Map<String, String> headers, String idPatient, Integer idPhysician, Date date);

    void updateConsultation(Map<String, String> headers, String id, ConsultationPatchDTO consultationPatch);

    void updateInvestigation(Map<String, String> headers, String idConsultation, String idInvestigation, Investigation investigation);

    EntityModel<Investigation> getInvestigation(Map<String, String> headers, String idConsultation, String idInvestigation);
}
