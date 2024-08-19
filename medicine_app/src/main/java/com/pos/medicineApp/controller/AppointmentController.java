package com.pos.medicineApp.controller;

import com.pos.medicineApp.interfaces.services.IAppointmentService;
import com.pos.medicineApp.interfaces.services.IAuthorizingService;
import com.pos.medicineApp.model.Appointment;
import com.pos.medicineApp.model.AppointmentStatus;
import com.pos.medicineApp.view.dto.AppointmentDate;
import com.pos.medicineApp.view.dto.PhysicianAppointmentStatusDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

import static com.pos.medicineApp.view.dto.PhysicianAppointmentStatus.ATTENDED;

@RestController
@RequestMapping("/api/medical_office")
@Validated
@RequiredArgsConstructor
public class AppointmentController {

    final private IAppointmentService appointmentService;
    final private IAuthorizingService authorizingService;

    // region patient view

    @Operation(summary = "Get all appointments for patient by specifed id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments found/resource empty"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/patients/{id_patient}/physicians")
    @ResponseBody
    public ResponseEntity<?> getPatientAppointments(@RequestHeader Map<String, String> headers, @PathVariable String id_patient) {
        authorizingService.authorizePatient(headers, id_patient);
        return new ResponseEntity<>(appointmentService.findPatientAppointments(id_patient), HttpStatus.OK);
    }

    @Operation(summary = "Get all appointments for patient by specifed id, date and type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments found/resource empty"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(value = "/patients/{id_patient}", params = {"date", "type"})
    @ResponseBody
    public ResponseEntity<?> getPatientAppointmentsByDateAndType(
            @RequestHeader Map<String, String> headers,
            @RequestParam(value = "date", required = false) @Min(1) @Max(31) Integer date,
            @RequestParam(value = "type", required = false) @Pattern(regexp = "day|month") String type,
            @PathVariable String id_patient) {
        authorizingService.authorizePatient(headers, id_patient);
        return new ResponseEntity<>(appointmentService.findAppointmentsByPatientIdAndDate_DayOrMonth(id_patient, type, date), HttpStatus.OK);
    }

    @Operation(summary = "Get all appointments for patient by specifed id and date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments found/resource empty"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(value = "/patients/{id_patient}", params = {"date"})
    @ResponseBody
    public ResponseEntity<?> getPatientAppointmentsByDate(
            @RequestHeader Map<String, String> headers,
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date date,
            @PathVariable String id_patient) {
        authorizingService.authorizePatient(headers, id_patient);
        return new ResponseEntity<>(appointmentService.findAppointmentsByPatientIdAndDate(id_patient, date), HttpStatus.OK);
    }

    @Operation(summary = "Get appointment for patient by specifed id, physician id and date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment found"),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/patients/{id_patient}/physicians/{id_physician}/{appointment_date}")
    @ResponseBody
    public ResponseEntity<?> getAppointment(
            @RequestHeader Map<String, String> headers,
            @PathVariable String id_patient,
            @PathVariable Integer id_physician,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date appointment_date) {
        authorizingService.authorizePatient(headers, id_patient);
        return new ResponseEntity<>(appointmentService.getAppointment("patient", id_physician, id_patient, appointment_date), HttpStatus.OK);
    }

    @Operation(summary = "Delete appointment", description = "Delete an appointment for patient by specifed id, physician id and date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment deleted"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @DeleteMapping("/patients/{id_patient}/physicians/{id_physician}/{date}")
    @ResponseBody
    public ResponseEntity<?> deleteAppointment(
            @RequestHeader Map<String, String> headers,
            @PathVariable String id_patient, @PathVariable Integer id_physician,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date date) {
        authorizingService.authorizePatient(headers, id_patient);
        appointmentService.updateAppointment(id_patient, id_physician, date, AppointmentStatus.CANCELED);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Create appointment", description = "Create an appointment for patient by specifed id, physician id and date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment created"),
            @ApiResponse(responseCode = "404", description = "Patient or physician not found"),
            @ApiResponse(responseCode = "409", description = "Appointment already exists"),
            @ApiResponse(responseCode = "422", description = "Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping("/patients/{id_patient}/physicians/{id_physician}")
    @ResponseBody
    public ResponseEntity<?> createAppointment(
            @RequestHeader Map<String, String> headers,
            @PathVariable String id_patient, @PathVariable Integer id_physician,
            @RequestBody AppointmentDate appointmentDate) {
        authorizingService.authorizePatient(headers, id_patient);
        return new ResponseEntity<>(appointmentService.addAppointment(id_patient, id_physician, appointmentDate.getDate()), HttpStatus.OK);
    }

    // endregion

    // region physician view


    @Operation(summary = "Get all appointments/patients for physician by specifed id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments found/resouce is empty"),
            @ApiResponse(responseCode = "404", description = "Physician not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/physicians/{id_physician}/patients")
    @ResponseBody
    public ResponseEntity<?> getPhysicianAppointmentsOrPatients(
            @RequestHeader Map<String, String> headers,
            @PathVariable Integer id_physician,
            @RequestParam(value = "patient", defaultValue = "false") Boolean patients) {
        authorizingService.authorizePhysician(headers, id_physician);
        if (patients) {
            return new ResponseEntity<>(appointmentService.getPatientsByPhysician(id_physician), HttpStatus.OK);
        }
        return new ResponseEntity<>(appointmentService.findAppointmentsByPhysicianId(id_physician), HttpStatus.OK);
    }

    @Operation(summary = "Get physician's patient appointments by specifed id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments found"),
            @ApiResponse(responseCode = "404", description = "Physician or patient not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/physicians/{id_physician}/patients/{id_patient}")
    @ResponseBody
    public ResponseEntity<?> getPhysicianAppointmentsByPatient(
            @RequestHeader Map<String, String> headers,
            @PathVariable Integer id_physician,
            @PathVariable String id_patient) {
        authorizingService.authorizePhysician(headers, id_physician);
        return new ResponseEntity<>(appointmentService.findAppointmentsByPhysicianIdAndPatientId(id_physician, id_patient), HttpStatus.OK);
    }

    @Operation(summary = "Update appointment status",
            description = "Update an appointment status to attended or nattended for patient by specifed id, physician id and date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment updated under status attended"),
            @ApiResponse(responseCode = "204", description = "Appointment updated under status nattended"),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "409", description = "Invalid appointment status"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PatchMapping("/physicians/{id_physician}/patients/{id_patient}/{appointmentDate}")
    @ResponseBody
    public ResponseEntity<?> updateAppointmentStatus(
            @RequestHeader Map<String, String> headers,
            @PathVariable String id_patient, @PathVariable Integer id_physician,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date appointmentDate,
            @RequestBody PhysicianAppointmentStatusDTO appointmentStatus) {
        // status 200 is returned because when the appointment is updated to status "ATTENDED", the resource
        // status allows the client to add a consultation
        // when appointment is updated to status "NATTENDED", the resource won't allow the client to add a consultation
        authorizingService.authorizePhysician(headers, id_physician);
        EntityModel<Appointment> updatedAppointment = appointmentService.updateAppointment(id_patient, id_physician, appointmentDate,
                appointmentStatus.getStatus().getAppointmentStatus());
        if (appointmentStatus.getStatus() == ATTENDED) {
            return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get physician's patient appointment by specifed id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment found"),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/physicians/{id_physician}/patients/{id_patient}/{appointment_date}")
    @ResponseBody
    public ResponseEntity<?> getAppointment(
            @RequestHeader Map<String, String> headers,
            @PathVariable Integer id_physician,
            @PathVariable String id_patient,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date appointment_date
    ) {
        authorizingService.authorizePhysician(headers, id_physician);
        return new ResponseEntity<>(appointmentService.getAppointment("physician", id_physician, id_patient, appointment_date), HttpStatus.OK);
    }

    @Operation(summary = "Get all dates for appointments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dates found/resource is empty"),
            @ApiResponse(responseCode = "404", description = "Physician not found")
    })
    @GetMapping(value = "/physicians/{id_physician}/patients/dates", params = {"date"})
    @ResponseBody
    public ResponseEntity<?> getDatesForAppointments(
            @PathVariable Integer id_physician,
            @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date appointment_date) {
        return new ResponseEntity<>(appointmentService.getDatesForAppointments(id_physician, appointment_date), HttpStatus.OK);
    }

    // endregion
}
