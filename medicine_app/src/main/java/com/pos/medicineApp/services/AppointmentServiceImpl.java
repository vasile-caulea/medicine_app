package com.pos.medicineApp.services;

import com.pos.medicineApp.controller.AppointmentController;
import com.pos.medicineApp.exceptions.ConflictException;
import com.pos.medicineApp.exceptions.NotFoundException;
import com.pos.medicineApp.interfaces.services.IAppointmentService;
import com.pos.medicineApp.model.*;
import com.pos.medicineApp.repository.AppointmentRepository;
import com.pos.medicineApp.repository.PatientRepository;
import com.pos.medicineApp.repository.PhysicianRepository;
import com.pos.medicineApp.view.hateoas.PatientAppointmentHateoas;
import com.pos.medicineApp.view.hateoas.PatientHateoas;
import com.pos.medicineApp.view.hateoas.PhysicianAppointmentHateoas;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@SuppressWarnings("DataFlowIssue")
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements IAppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final PhysicianRepository physicianRepository;
    private final PatientAppointmentHateoas patientAppointmentHateoas;
    private final PhysicianAppointmentHateoas physicianAppointmentHateoas;
    private final PatientHateoas patientHateoas;

    @Override
    public CollectionModel<EntityModel<Appointment>> findAppointmentsByPatientIdAndDate_DayOrMonth(String idPatient, String dateType, Integer date) {
        if (!patientRepository.existsById(idPatient)) throw new NotFoundException("Patient not found");
        List<Appointment> appointments = null;
        switch (dateType) {
            case "day" -> appointments = appointmentRepository.findAppointmentsByPatientIdAndDate_Day(idPatient, date);
            case "month" ->
                    appointments = appointmentRepository.findAppointmentsByPatientIdAndDate_Month(idPatient, date);
        }
        Link[] links = {
                linkTo(methodOn(AppointmentController.class).getPatientAppointmentsByDateAndType(null, date, dateType, idPatient)).withSelfRel(),
                linkTo(methodOn(AppointmentController.class).getPatientAppointments(null, idPatient)).withRel("parent"),
                linkTo(methodOn(AppointmentController.class).createAppointment(null, idPatient, null, null)).withRel("add-appointment").withType("POST")
        };
        if (null == appointments || appointments.isEmpty())
            return CollectionModel.of(new ArrayList<>(), links);
        return CollectionModel.of(appointments.stream().map(patientAppointmentHateoas::toModel).toList(), links);
    }

    @Override
    public CollectionModel<EntityModel<Appointment>> findAppointmentsByPatientIdAndDate(String id_patient, Date date) {
        if (!patientRepository.existsById(id_patient)) throw new NotFoundException("Patient not found");
        List<Appointment> appointments = appointmentRepository.findAppointmentsByPatientIdAndDate(id_patient, date);

        Link[] links = {
                linkTo(methodOn(AppointmentController.class).getPatientAppointmentsByDate(null, date, id_patient)).withSelfRel(),
                linkTo(methodOn(AppointmentController.class).getPatientAppointments(null, id_patient)).withRel("parent"),
                linkTo(methodOn(AppointmentController.class).createAppointment(null, id_patient, null, null)).withRel("add-appointment").withType("POST")
        };
        if (appointments.isEmpty())
            return CollectionModel.of(new ArrayList<>(), links);
        return CollectionModel.of(appointments.stream().map(patientAppointmentHateoas::toModel).toList(), links);
    }

    @Override
    public CollectionModel<EntityModel<Appointment>> findPatientAppointments(String id) {
        if (!patientRepository.existsById(id)) throw new NotFoundException("Patient not found");
        List<Appointment> appointments = appointmentRepository.findAppointmentsByPatientId(id);
        Link[] links = {
                linkTo(methodOn(AppointmentController.class).getPatientAppointments(null, id)).withSelfRel(),
                linkTo(methodOn(AppointmentController.class).createAppointment(null, id, null, null)).withRel("add-appointment").withType("POST"),
        };
        if (appointments.isEmpty())
            return CollectionModel.of(new ArrayList<>(), links);
        return CollectionModel.of(appointments.stream().map(patientAppointmentHateoas::toModel).toList(), links);
    }

    @Override
    public EntityModel<Appointment> updateAppointment(String patientId, Integer physicianId, Date date, AppointmentStatus status) {
        AppointmentPK pk = new AppointmentPK(patientId, physicianId, date);
        Optional<Appointment> appointment = appointmentRepository.findById(pk);
        if (appointment.isEmpty()) throw new NotFoundException("Appointment not found");

        appointment.get().setAppointmentStatus(status);
        Appointment appointmentR = appointmentRepository.save(appointment.get());
        if (status == AppointmentStatus.CANCELED) return patientAppointmentHateoas.toModel(appointmentR);
        else return physicianAppointmentHateoas.toModel(appointmentR);
    }

    @Override
    public EntityModel<Appointment> addAppointment(String patientId, Integer physicianId, Date date) {
        AppointmentPK pk = new AppointmentPK(patientId, physicianId, date);
        if (appointmentRepository.existsById(pk)) throw new ConflictException("Appointent already exists");

        Optional<Patient> patient = patientRepository.findById(patientId);
        if (patient.isEmpty()) throw new NotFoundException("Could not find patient " + patientId);

        Optional<Physician> physician = physicianRepository.findById(physicianId);
        if (physician.isEmpty()) throw new NotFoundException("Could not find physician with id = " + physicianId);

        // check if date is in the future
        if (date.before(new Date())) throw new ConflictException("Date must be in the future");

        // check if user has already an appointment in that date and time
        if (appointmentRepository.existsByPatientIdAndDateAndTime(patientId, date)) {
            throw new ConflictException("Patient has already an appointment in that date and time");
        }

        Appointment appointment = new Appointment(patient.get(), physician.get(), date, null);
        Appointment appointmentRepo = appointmentRepository.save(appointment);
        return patientAppointmentHateoas.toModel(appointmentRepo);
    }

    @Override
    public CollectionModel<EntityModel<Patient>> getPatientsByPhysician(Integer idp) {
        if (!physicianRepository.existsById(idp)) throw new NotFoundException("Physician not found");
        List<Patient> patients = appointmentRepository.findUniquePatientsByPhysicianId(idp);
        // selfrel because it is the parent resource
        Link[] links = {
                linkTo(methodOn(AppointmentController.class).getPhysicianAppointmentsOrPatients(null, idp, true)).withSelfRel(),
                linkTo(methodOn(AppointmentController.class).getPhysicianAppointmentsOrPatients(null, null, null)).withRel("parent")
        };
        if (patients.isEmpty())
            return CollectionModel.of(new ArrayList<>(), links);
        return CollectionModel.of(patients.stream().map(patientHateoas::toModel).toList(), links);
    }

    @Override
    public CollectionModel<EntityModel<Appointment>> findAppointmentsByPhysicianId(Integer id) {
        if (!physicianRepository.existsById(id)) throw new NotFoundException("Physician not found");
        // selfrel because it is the parent resource, physician cannot add appointments
        List<Appointment> appointments = appointmentRepository.findAppointmentsByPhysicianId(id);
        Link[] links = {
                linkTo(methodOn(AppointmentController.class).getPhysicianAppointmentsOrPatients(null, id, null)).withSelfRel(),
                linkTo(methodOn(AppointmentController.class).getPhysicianAppointmentsOrPatients(null, null, null)).withRel("parent")
        };
        if (appointments.isEmpty())
            return CollectionModel.of(new ArrayList<>(), links);
        return CollectionModel.of(appointments.stream().map(physicianAppointmentHateoas::toModel).toList(), links);
    }

    @Override
    public CollectionModel<EntityModel<Appointment>> findAppointmentsByPhysicianIdAndPatientId(Integer idPhysician, String idPatient) {
        if (!physicianRepository.existsById(idPhysician)) throw new NotFoundException("Physician not found");
        if (!patientRepository.existsById(idPatient)) throw new NotFoundException("Patient not found");
        List<Appointment> appointments = appointmentRepository.findAppointmentsByPhysicianIdAndPatientId(idPhysician, idPatient);
        Link[] links = {
                linkTo(methodOn(AppointmentController.class).getPhysicianAppointmentsByPatient(null, idPhysician, idPatient)).withSelfRel(),
                linkTo(methodOn(AppointmentController.class).getPhysicianAppointmentsOrPatients(null, idPhysician, false)).withRel("parent")
        };
        if (appointments.isEmpty())
            return CollectionModel.of(new ArrayList<>(), links);
        return CollectionModel.of(appointments.stream().map(physicianAppointmentHateoas::toModel).toList(), links);
    }

    @Override
    public EntityModel<Appointment> getAppointment(String type, Integer idPhysician, String idPatient, Date appointmentDate) {
        AppointmentPK pk = new AppointmentPK(idPatient, idPhysician, appointmentDate);
        Optional<Appointment> appointment = appointmentRepository.findById(pk);
        if (appointment.isEmpty()) throw new NotFoundException("Appointment not found");
        if (type.equals("patient")) return patientAppointmentHateoas.toModel(appointment.get());
        return physicianAppointmentHateoas.toModel(appointment.get());
    }

    @Override
    public CollectionModel<Date> getDatesForAppointments(Integer idPhysician, Date appointmentDate) {
        List<Date> dates = appointmentRepository.findDatesForAppointments(idPhysician, appointmentDate);
        Link[] links = {
                linkTo(methodOn(AppointmentController.class).getDatesForAppointments(idPhysician, appointmentDate)).withSelfRel(),
                linkTo(methodOn(AppointmentController.class).getPhysicianAppointmentsOrPatients(null, idPhysician, false)).withRel("parent")
        };
        if (dates.isEmpty())
            return CollectionModel.of(new ArrayList<>(), links);
        return CollectionModel.of(dates, links);
    }
}
