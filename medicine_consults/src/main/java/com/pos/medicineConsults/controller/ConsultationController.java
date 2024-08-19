package com.pos.medicineConsults.controller;

import com.pos.medicineConsults.interfaces.services.IConsultationService;
import com.pos.medicineConsults.model.ConsultationPatchDTO;
import com.pos.medicineConsults.model.POJO.Consultation;
import com.pos.medicineConsults.model.POJO.Investigation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/medical_office/consultations")
public class ConsultationController {

    private final IConsultationService consultationService;

    public ConsultationController(IConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @Operation(summary = "Create a new consultation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Consultation created"),
            @ApiResponse(responseCode = "409", description = "Consultation already exists"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping
    @ResponseBody
    public ResponseEntity<?> createConsultation(
            @RequestHeader Map<String, String> headers,
            @RequestBody Consultation consultation) {
        return new ResponseEntity<>(consultationService.addConsultation(headers, consultation), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a consultation by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consultation found"),
            @ApiResponse(responseCode = "404", description = "Consultation not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> getConsultation(@RequestHeader Map<String, String> headers, @PathVariable String id) {
        return new ResponseEntity<>(consultationService.getConsultation(headers, id), HttpStatus.OK);
    }

    @Operation(summary = "Get a consultation by patient id, doctor id and date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consultation found"),
            @ApiResponse(responseCode = "404", description = "Consultation not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/{idPatient}/{idPhysician}/{date}")
    public ResponseEntity<?> getConsultation(
            @RequestHeader Map<String, String> headers,
            @PathVariable String idPatient,
            @PathVariable Integer idPhysician,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date date) {
        return new ResponseEntity<>(consultationService.getConsultation(headers, idPatient, idPhysician, date), HttpStatus.OK);
    }

    @Operation(summary = "Get investigation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Investigation found"),
            @ApiResponse(responseCode = "404", description = "Consultation/Investigation not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/{id_consultation}/investigations/{id_investigation}")
    @ResponseBody
    public ResponseEntity<?> getInvestigation(
            @RequestHeader Map<String, String> headers,
            @PathVariable String id_consultation,
            @PathVariable String id_investigation) {
        return new ResponseEntity<>(consultationService.getInvestigation(headers, id_consultation, id_investigation), HttpStatus.OK);
    }

    @Operation(summary = "Add an investigation to a consultation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Investigation added"),
            @ApiResponse(responseCode = "404", description = "Consultation not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PatchMapping("/{id}/investigations")
    @ResponseBody
    public ResponseEntity<?> addInvestigation(
            @RequestHeader Map<String, String> headers,
            @PathVariable String id,
            @RequestBody Investigation investigation) {
        return new ResponseEntity<>(consultationService.addInvestigation(headers, id, investigation), HttpStatus.OK);
    }

    @Operation(summary = "Update an investigation in a consultation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Investigation updated"),
            @ApiResponse(responseCode = "404", description = "Consultation or Investigation not found"),
            @ApiResponse(responseCode = "422", description = "Investigation body is invalid"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PatchMapping("/{id_consultation}/investigations/{id_investigation}")
    @ResponseBody
    public ResponseEntity<?> updateInvestigation(
            @RequestHeader Map<String, String> headers,
            @PathVariable String id_consultation,
            @PathVariable String id_investigation,
            @RequestBody Investigation investigation) {
        consultationService.updateInvestigation(headers, id_consultation, id_investigation, investigation);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Update a consultation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Consultation updated"),
            @ApiResponse(responseCode = "404", description = "Consultation not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> updateConsultation(
            @RequestHeader Map<String, String> headers,
            @PathVariable String id,
            @RequestBody ConsultationPatchDTO consultationPatch) {
        consultationService.updateConsultation(headers, id, consultationPatch);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
