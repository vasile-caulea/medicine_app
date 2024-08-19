package com.pos.medicineApp.controller;

import com.pos.medicineApp.exceptions.UnprocessableContentException;
import com.pos.medicineApp.interfaces.services.IAuthorizingService;
import com.pos.medicineApp.interfaces.services.IPatientService;
import com.pos.medicineApp.model.Patient;
import com.pos.medicineApp.view.dto.PatientPatchDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/medical_office/patients")
@RequiredArgsConstructor
public class PatientController {

    private final IPatientService patientService;
    private final IAuthorizingService authorizingService;

    @Operation(summary = "Add new patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient successfully added"),
            @ApiResponse(responseCode = "422", description = "Request body is not valid"),
            @ApiResponse(responseCode = "409", description = "New resource conflict with existing resource"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PutMapping
    @ResponseBody
    public ResponseEntity<?> addPatient(@RequestHeader Map<String, String> headers, @RequestBody Patient patient) {
        if (patient.getIdUser() == null) {
            throw new UnprocessableContentException("User id is required");
        }
        authorizingService.authorizeCreatePatient(headers, patient.getIdUser());
        return new ResponseEntity<>(patientService.add(patient), HttpStatus.CREATED);
    }

    @Operation(summary = "Get patient by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient was found"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> getPatient(@RequestHeader Map<String, String> headers, @PathVariable String id) {
        authorizingService.authorizePatient(headers, id);
        return new ResponseEntity<>(patientService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "Get patient by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient was found"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/user/{userId}")
    @ResponseBody
    public ResponseEntity<?> getPatientByUserId(
            @RequestHeader Map<String, String> headers, @PathVariable Integer userId) {
        authorizingService.authorizePatientByUserId(headers, userId);
        return new ResponseEntity<>(patientService.findByUserId(userId), HttpStatus.OK);
    }

    @Operation(summary = "Update patients's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Update succeeded"),
            @ApiResponse(responseCode = "409", description = "New resource conflict with existing resource"),
            @ApiResponse(responseCode = "422", description = "Invalid update data"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> updatePatient(@RequestHeader Map<String, String> headers, @RequestBody PatientPatchDTO patientPatch, @PathVariable String id) {
        authorizingService.authorizePatient(headers, id);
        patientService.update(id, patientPatch);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Delete patient by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient was found and deleted"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deletePatient(@RequestHeader Map<String, String> headers, @PathVariable String id) {
        authorizingService.authorizePatient(headers, id);
        patientService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
