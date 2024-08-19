package com.pos.medicineApp;

import com.pos.medicineApp.model.Patient;
import com.pos.medicineApp.repository.PatientRepository;
import org.aspectj.util.UtilClassLoader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

@SpringBootTest(classes = MedicineApp.class)
public class PatientRepositoryTest {

    @Autowired
    PatientRepository patientRepository;

    @Test
    void TestPatchMethod() {
        Patient patient = new Patient();
        patient.setCnp("1234567890123");
        patient.setIdUser(12);
        patient.setLastName("John");
        patient.setFirstName("Marcus");
        patient.setEmail("john.marcus@mail.com");
        patient.setPhoneNumber("0743242342");

        System.out.println(patientRepository.save(patient));

        Patient patientPatch = new Patient();
        patientPatch.setEmail("marcus.john@mail.com");

        System.out.println(patientRepository.findByCnpAndIsActive(patient.getCnp(), true));
        // System.out.println(patient);
        // System.out.println(patientRepository.updatePatientByCnp(patient.getCnp(), patient));

    }
}
