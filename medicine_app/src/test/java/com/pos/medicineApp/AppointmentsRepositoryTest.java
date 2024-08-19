package com.pos.medicineApp;

import com.pos.medicineApp.model.Appointment;
import com.pos.medicineApp.model.AppointmentPK;
import com.pos.medicineApp.model.Patient;
import com.pos.medicineApp.model.Physician;
import com.pos.medicineApp.repository.AppointmentRepository;
import com.pos.medicineApp.repository.PatientRepository;
import com.pos.medicineApp.repository.PhysicianRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = MedicineApp.class)
public class AppointmentsRepositoryTest {

    @Autowired
    PhysicianRepository physiciansRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Test
    void TestInserting() {

        Patient patient = new Patient();
        patient.setCnp("1030312890123");
        patient.setIdUser(12);
        patient.setLastName("John");
        patient.setFirstName("Marcus");
        patient.setEmail("john.marcus@mail.com");
        patient.setPhoneNumber("0743242342");

        Physician physician = new Physician();
        physician.setIdUser(14);
        physician.setLastName("Martel");
        physician.setFirstName("John");
        physician.setEmail("marcus.joh@johb.com");
        physician.setPhoneNumber("0789990065");
        physician.setSpecialization("ORL");


        Patient newPatient = patientRepository.save(patient);
        System.out.println(newPatient);
        Physician newPhysician = physiciansRepository.save(physician);

        physician = new Physician();
        physician.setIdUser(14);
        physician.setLastName("Marcus");
        physician.setFirstName("John");
        physician.setEmail("marcus.john@johb.com");
        physician.setPhoneNumber("0789990066");
        physician.setSpecialization("ORL");

        newPhysician = physiciansRepository.save(physician);


        Appointment app = new Appointment();
        Date date = Date.valueOf("1990-09-12");
        app.setPatient(newPatient);
        app.setPhysician(newPhysician);
        app.setDate(date);
        Appointment newApp = appointmentRepository.save(app);

        List<Appointment> repoAppointment = appointmentRepository.findAppointmentByPatientId(patient.getCnp());
        System.out.println(newApp);

        System.out.println("Repository get");
        repoAppointment.forEach((n) -> System.out.println(n.getPatient()));

        Optional<Appointment> appId = appointmentRepository.findById(new AppointmentPK(patient.getCnp(), physician.getIdPhysician(), date));

        System.out.println(appId);

        List<Appointment> appointments = appointmentRepository.findAppointmentsByPatientIdAndDate_Month("1234567890123", 9);
        appointments.forEach(System.out::println);
        System.out.println("-------------------------");

        appointments = appointmentRepository.findAppointmentsByPatientIdAndDate_Day("1234567890123", 12);
        appointments.forEach(System.out::println);
        System.out.println("-------------------------");

        appointments = appointmentRepository.findAppointmentsByPatientIdAndDate(patient.getCnp(), date);
        appointments.forEach(System.out::println);
        System.out.println("-------------------------");

        System.out.println("-------------------------");

        List<Physician> value = physiciansRepository.findPhysiciansBySpecializationContainingIgnoreCase("OR");
        value.forEach((x) -> System.out.println(x.getLastName()));

        System.out.println("-------------------------");
        Pageable paging = PageRequest.of(1, 1);
        List<Physician> physicians = physiciansRepository.findAll(paging).toList();
        physicians.forEach((x) -> System.out.println(x.getLastName()));


        System.out.println("-------------------------");
        value = physiciansRepository.findPhysiciansByLastNameContainingIgnoreCase("Marc");
        value.forEach((x) -> System.out.println(x.getLastName()));
    }

    @Test
    void TestInserting2() {
        Patient patient = new Patient();
        patient.setCnp("1234567890123");
        patient.setIdUser(12);
        patient.setLastName("John");
        patient.setFirstName("Marcus");
        patient.setEmail("john.marcus@mail.com");
        patient.setPhoneNumber("0743242342");
        patient.setBirthdate(Date.valueOf("1990-12-09"));

        Physician physician = new Physician();
        physician.setIdUser(14);
        physician.setLastName("Marcus");
        physician.setFirstName("John");
        physician.setEmail("marcus.john@johb.com");
        physician.setPhoneNumber("0789990065");
        physician.setSpecialization("ORL");

        Patient newPatient = patientRepository.save(patient);
        physiciansRepository.save(physician);
        Appointment app = new Appointment();
        Date date = Date.valueOf("1990-09-12");
        app.setPatient(newPatient);
        app.setPhysician(physician);
        app.setDate(date);
        appointmentRepository.save(app);


    }
}
