package com.pos.medicineApp.controller;

import com.pos.medicineApp.interfaces.services.IAuthorizingService;
import com.pos.medicineApp.interfaces.services.IPhysicianService;
import com.pos.medicineApp.model.Physician;
import com.pos.medicineApp.view.dto.PhysicianPatchDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/medical_office/physicians")
@Validated
@RequiredArgsConstructor
public class PhysicianController {

    private final IPhysicianService physicianService;
    private final IAuthorizingService authorizingService;


    @Operation(summary = "Add new physician")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Physician successfully added"),
            @ApiResponse(responseCode = "422", description = "Request body is not valid"),
            @ApiResponse(responseCode = "409", description = "New resource conflict with existing resource"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping
    @ResponseBody
    public ResponseEntity<?> addPhysician(@RequestHeader Map<String, String> headers, @RequestBody Physician physician) {
        authorizingService.authorizeCreatePhysician(headers, physician.getIdUser());
        return new ResponseEntity<>(physicianService.addPhysician(physician), HttpStatus.CREATED);
    }

    @Operation(summary = "Get physician by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Physician was found"),
            @ApiResponse(responseCode = "404", description = "Physician not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/user/{userId}")
    @ResponseBody
    public ResponseEntity<?> getPhysicianByUserId(
            @RequestHeader Map<String, String> headers, @PathVariable Integer userId) {
        authorizingService.authorizePhysicianByUserId(headers, userId);
        return new ResponseEntity<>(physicianService.findByUserId(userId), HttpStatus.OK);
    }

    @Operation(summary = "Get physicians")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phyisicians found"),
            @ApiResponse(responseCode = "404", description = "Physicians not found")
    })
    @GetMapping
    @ResponseBody
    public ResponseEntity<?> getPhysicians(
            @RequestParam Map<String, String> queryParams,
            @RequestParam(name = "page", required = false) @Min(0) Integer page,
            @RequestParam(name = "items_per_page", defaultValue = "5", required = false) @Min(1) Integer itemsPerPage
    ) {
        return new ResponseEntity<>(physicianService.findAllWithFilters(queryParams, page, itemsPerPage), HttpStatus.OK);
    }


    @Operation(summary = "Get physician by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phyisician found"),
            @ApiResponse(responseCode = "404", description = "Physician not found")
    })
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> getPhysician(@PathVariable Integer id) {
        return new ResponseEntity<>(physicianService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "Update physician's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Update succeeded"),
            @ApiResponse(responseCode = "409", description = "New resource conflict with existing resource"),
            @ApiResponse(responseCode = "422", description = "Invalid update data"),
            @ApiResponse(responseCode = "404", description = "Physician not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> updatePhysician(@RequestHeader Map<String, String> headers, @PathVariable Integer id, @RequestBody PhysicianPatchDTO physicianPatch) {
        authorizingService.authorizePhysician(headers, id);
        physicianService.update(id, physicianPatch);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
