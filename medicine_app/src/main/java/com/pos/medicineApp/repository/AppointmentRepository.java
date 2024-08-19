package com.pos.medicineApp.repository;

import com.pos.medicineApp.model.Appointment;
import com.pos.medicineApp.model.AppointmentPK;
import com.pos.medicineApp.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, AppointmentPK> {

    @Query("SELECT app FROM Appointment app WHERE app.patient.cnp = ?1")
    List<Appointment> findAppointmentByPatientId(String patientId);

    @Query("SELECT app FROM Appointment app WHERE app.patient.cnp = ?1 AND MONTH(app.date) = ?2")
    List<Appointment> findAppointmentsByPatientIdAndDate_Month(String PatientId, Integer month);

    @Query("SELECT app FROM Appointment app WHERE app.patient.cnp = ?1 AND DAY(app.date) = ?2")
    List<Appointment> findAppointmentsByPatientIdAndDate_Day(String PatientId, Integer day);

    @Query("SELECT app FROM Appointment app WHERE app.patient.cnp = ?1 AND DATE_FORMAT(app.date, '%Y-%m-%d %H:%i') = DATE_FORMAT(?2, '%Y-%m-%d %H:%i')")
    List<Appointment> findAppointmentsByPatientIdAndDate(String PatientId, Date date);

    @Query("SELECT app FROM Appointment app WHERE app.patient.cnp = ?1")
    List<Appointment> findAppointmentsByPatientId(String PatientId);

    @Query("SELECT app FROM Appointment app WHERE app.physician.idPhysician=?1")
    List<Appointment> findAppointmentsByPhysicianId(Integer physicianId);


    @Query("SELECT DISTINCT app.patient FROM Appointment app WHERE app.physician.idPhysician = ?1")
    List<Patient> findUniquePatientsByPhysicianId(Integer physicianId);

    @Query("SELECT app FROM Appointment app WHERE app.physician.idPhysician = ?1 AND app.patient.cnp = ?2")
    List<Appointment> findAppointmentsByPhysicianIdAndPatientId(Integer idPhysician, String idPatient);

    @Query("SELECT app.date FROM Appointment app WHERE app.physician.idPhysician = ?1 AND DATE(app.date) = DATE(?2)")
    List<Date> findDatesForAppointments(Integer idPhysician, Date appointmentDate);

    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM Appointment app WHERE app.patient.cnp = ?1 AND DATE_FORMAT(app.date, '%Y-%m-%d %H:%i') = DATE_FORMAT(?2, '%Y-%m-%d %H:%i')")
    boolean existsByPatientIdAndDateAndTime(String patientId, Date date);
}
