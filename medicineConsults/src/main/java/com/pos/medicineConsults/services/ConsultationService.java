package com.pos.medicineConsults.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.pos.medicineConsults.controller.ConsultationController;
import com.pos.medicineConsults.exceptions.ConflictException;
import com.pos.medicineConsults.exceptions.ForbiddenException;
import com.pos.medicineConsults.exceptions.NotFoundException;
import com.pos.medicineConsults.exceptions.UnProcessableEntityException;
import com.pos.medicineConsults.interfaces.services.IAuthorizingService;
import com.pos.medicineConsults.interfaces.services.IConsultationService;
import com.pos.medicineConsults.interfaces.services.IRequestService;
import com.pos.medicineConsults.model.ConsultationPatchDTO;
import com.pos.medicineConsults.model.POJO.Consultation;
import com.pos.medicineConsults.model.POJO.Investigation;
import com.pos.medicineConsults.repository.ConsultationRepository;
import com.pos.medicineConsults.repository.InvestigationRepository;
import com.pos.medicineConsults.utils.UserType;
import com.pos.medicineConsults.view.hateoas.ConsultationHateoas;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.pos.medicineConsults.utils.Utils.getJsonNode;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class ConsultationService implements IConsultationService {

    private final ConsultationRepository consultationRepository;
    private final InvestigationRepository investigationRepository;
    private final ConsultationHateoas consultationHateoas;
    private final ModelMapper modelMapper;
    private final IRequestService requestService;
    private final IAuthorizingService authorizingService;


    @Override
    public EntityModel<Consultation> addConsultation(Map<String, String> headers, Consultation consultation) {
        authorizingService.authorizeRolePhysician(headers);

        String response = requestService.getAppointmentForUserType(
                UserType.PHYSICIAN, headers.get("authorization"), consultation.getIdPatient(),
                consultation.getIdPhysician(), consultation.getDate());
        JsonNode node = getJsonNode(response);
        if (null != node && !node.get("appointmentStatus").asText().equals("ATTENDED"))
            throw new ConflictException("The patient has not attended the appointment yet");

        if (consultationRepository.existsByIdPatientAndIdPhysicianAndDate(consultation.getIdPatient(),
                consultation.getIdPhysician(), consultation.getDate()))
            throw new ConflictException("The consultation with specified data already exists.");
        return EntityModel.of(consultationRepository.save(consultation));
    }

    @Override
    public EntityModel<Consultation> getConsultation(Map<String, String> headers, String idConsult) {
        var user = authorizingService.authorizeRolePhysicianPatient(headers);

        if (!user.getRoles().contains("physician") && !user.getRoles().contains("patient"))
            throw new ForbiddenException();

        Optional<Consultation> consultationR = consultationRepository.findById(idConsult);
        if (consultationR.isEmpty()) throw new NotFoundException("Consultation with id " + idConsult + " not found");
        Consultation consultation = consultationR.get();
        String ignore = requestService.getAppointmentForUserType(
                (user.getRoles().contains("physician") ? UserType.PHYSICIAN : UserType.PATIENT),
                headers.get("authorization"), consultation.getIdPatient(), consultation.getIdPhysician(),
                consultation.getDate()
        );
        return consultationHateoas.toModel(consultationR.get());
    }

    @Override
    public EntityModel<Consultation> getConsultation(Map<String, String> headers, String idPatient, Integer idPhysician, Date date) {
        var user = authorizingService.authorizeRolePhysicianPatient(headers);
        String ignore = requestService.getAppointmentForUserType(
                (user.getRoles().contains("physician") ? UserType.PHYSICIAN : UserType.PATIENT),
                headers.get("authorization"), idPatient, idPhysician, date);
        Optional<Consultation> consultation = consultationRepository.findByIdPatientAndIdPhysicianAndDate(idPatient, idPhysician, date);
        if (consultation.isEmpty()) throw new NotFoundException("Consultation not found");
        return consultationHateoas.toModel(consultation.get());
    }

    @Override
    public void updateConsultation(Map<String, String> headers, String id, ConsultationPatchDTO consultationPatch) {
        authorizingService.authorizeRolePhysician(headers);
        Optional<Consultation> consultationR = consultationRepository.findById(id);
        if (consultationR.isEmpty()) throw new NotFoundException("Consultation with id " + id + " not found");

        Consultation consultation = consultationR.get();
        String ignore = requestService.getAppointmentForUserType(UserType.PHYSICIAN,
                headers.get("authorization"), consultation.getIdPatient(), consultation.getIdPhysician(),
                consultation.getDate()
        );
        if (consultationPatch.getDiagnosis() != null)
            consultation.setDiagnosis(consultationPatch.getDiagnosis());
        consultationRepository.save(consultation);
    }

    @Override
    public EntityModel<Consultation> addInvestigation(Map<String, String> headers, String id, Investigation investigation) {
        authorizingService.authorizeRolePhysician(headers);
        Optional<Consultation> consultationR = consultationRepository.findById(id);
        if (consultationR.isEmpty())
            throw new NotFoundException("Consultation with id " + id + " not found");

        Consultation consultation = consultationR.get();
        String ignore = requestService.getAppointmentForUserType(
                UserType.PHYSICIAN, headers.get("authorization"), consultation.getIdPatient(),
                consultation.getIdPhysician(), consultation.getDate());

        Investigation investigationR = investigationRepository.save(investigation);

        List<Investigation> investigations = consultation.getInvestigations();
        investigations.add(investigationR);
        consultation.setInvestigations(investigations);

        return consultationHateoas.toModel(consultationRepository.save(consultation));
    }

    @Override
    public void updateInvestigation(Map<String, String> headers, String idConsultation, String idInvestigation, Investigation investigation) {
        authorizingService.authorizeRolePhysician(headers);
        if (investigation.getName() == null && investigation.getDuration() == null && investigation.getResult() == null)
            throw new UnProcessableEntityException("At least one field is required");

        Optional<Consultation> consultationR = consultationRepository.findById(idConsultation);
        if (consultationR.isEmpty())
            throw new NotFoundException("Consultation with id " + idConsultation + " not found");
        Consultation consultation = consultationR.get();
        String ignore = requestService.getAppointmentForUserType(
                UserType.PHYSICIAN, headers.get("authorization"), consultation.getIdPatient(),
                consultation.getIdPhysician(), consultation.getDate());

        List<Investigation> investigations = consultationR.get().getInvestigations();
        for (Investigation existingInvestigation : investigations) {
            if (existingInvestigation.getId().equals(idInvestigation)) {
                modelMapper.map(investigation, existingInvestigation);
                investigationRepository.save(existingInvestigation);
                consultationRepository.save(consultationR.get());
                return;
            }
        }
    }

    @Override
    public EntityModel<Investigation> getInvestigation(Map<String, String> headers, String idConsultation, String idInvestigation) {
        var user = authorizingService.authorizeRolePhysicianPatient(headers);
        if (!user.getRoles().contains("physician") && !user.getRoles().contains("patient"))
            throw new ForbiddenException();
        Optional<Consultation> consultationR = consultationRepository.findById(idConsultation);
        if (consultationR.isEmpty()) throw new NotFoundException("Consultation with id " + idConsultation + " not found");
        Consultation consultation = consultationR.get();
        String ignore = requestService.getAppointmentForUserType(
                (user.getRoles().contains("physician") ? UserType.PHYSICIAN : UserType.PATIENT),
                headers.get("authorization"), consultation.getIdPatient(), consultation.getIdPhysician(),
                consultation.getDate()
        );

        Investigation investigation = null;
        for (Investigation i: consultation.getInvestigations()) {
            if (i.getId().equals(idInvestigation)) {
                investigation = i;
                break;
            }
        }
        if (investigation == null) throw new NotFoundException("Investigation with id " + idInvestigation + " not found");
        Link[] links = {
                linkTo(methodOn(ConsultationController.class).getInvestigation(null, idConsultation, idInvestigation)).withSelfRel(),
                linkTo(methodOn(ConsultationController.class).updateInvestigation(null, idConsultation, idInvestigation, null)).withRel("update").withType("PATCH"),
        };
        return EntityModel.of(investigation, links);
    }
}
