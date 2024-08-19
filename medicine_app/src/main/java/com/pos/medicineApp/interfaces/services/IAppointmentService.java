package com.pos.medicineApp.interfaces.services;

import com.pos.medicineApp.model.Appointment;
import com.pos.medicineApp.model.AppointmentStatus;
import com.pos.medicineApp.model.Patient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.Date;

public interface IAppointmentService {

    CollectionModel<EntityModel<Appointment>> findAppointmentsByPatientIdAndDate_DayOrMonth(String id, String type, Integer date);

    CollectionModel<EntityModel<Appointment>> findAppointmentsByPatientIdAndDate(String id, Date day);

    CollectionModel<EntityModel<Appointment>> findPatientAppointments(String idp);

    CollectionModel<EntityModel<Appointment>> findAppointmentsByPhysicianId(Integer id);

    EntityModel<Appointment> updateAppointment(String patientId, Integer physicianId, Date date, AppointmentStatus status);

    EntityModel<Appointment> addAppointment(String patientId, Integer physicianId, Date date);

    CollectionModel<EntityModel<Patient>> getPatientsByPhysician(Integer idp);

    CollectionModel<EntityModel<Appointment>> findAppointmentsByPhysicianIdAndPatientId(Integer idPhysician, String idPatient);

    EntityModel<Appointment> getAppointment(String type, Integer idPhysician, String idPatient, Date appointmentDate);

    CollectionModel<Date> getDatesForAppointments(Integer idPhysician, Date appointmentDate);
}
