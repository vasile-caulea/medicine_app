package com.pos.medicineApp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.medicineApp.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {

    @Autowired
    private MockMvc mvc;
    private Patient patient;
    private final ObjectMapper objMapper = new ObjectMapper();

    private final String urlTemplate = "/api/medical_office/patients";
    private final StatusResultMatchers statusResult = status();
    private final ResultMatcher isCreated = statusResult.isCreated();
    private final ResultMatcher isConflict = statusResult.isConflict();


    @BeforeEach
    public void setPatient() {
        patient = new Patient();
        patient.setCnp("1030312890123");
        patient.setIdUser(12);
        patient.setLastName("John");
        patient.setFirstName("Marcus");
        patient.setEmail("john.marcus@maail.com");
        patient.setPhoneNumber("0743242642");
    }

    private void savePatient(String requestBody, ResultMatcher expectedResult) throws Exception {
        mvc.perform(MockMvcRequestBuilders.put(urlTemplate).contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(expectedResult).andDo(print());
    }

    // test with valid data
    @Test
    public void testCnp() throws Exception {
        patient.setCnp("1030312890125");
        savePatient(objMapper.writeValueAsString(patient), isCreated);
    }

    // test with valid data
    @Test
    public void testCnpInvalid() throws Exception {
        patient.setCnp("1063312890125");
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }

    //region testing cnp field constraints
    @Test
    public void testCnpLengthGtThan13() throws Exception {
        patient.setCnp("12345678901230");
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }

    @Test
    public void testCnpLengthLessThan13() throws Exception {
        patient.setCnp("123456789012");
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }

    @Test
    void testEmptyCnp() throws Exception {
        patient.setCnp("");
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }

    @Test
    void testNullCnp() throws Exception {
        Patient patient1 = new Patient();
        savePatient(objMapper.writeValueAsString(patient1), isConflict);
    }

    //endregion

    //region testing last_name field constraints
    @Test
    void testLastNameWithLengthGtThan50() throws Exception {
        patient.setLastName("John Mike John Mike John Mike John Mike John Mike John Mike");
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }

    @Test
    void testEmptyLastName() throws Exception {
        patient.setLastName("");
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }

    @Test
    void testNullLastName() throws Exception {
        patient.setLastName(null);
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }

    @Test
    void testLastNameWithInvalidCharacters() throws Exception {
        patient.setLastName("mike__2?12");
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }
    //endregion

    //region testing first_name field constraints
    @Test
    void testFirstNameWithLengthGtThan50() throws Exception {
        patient.setFirstName("John Mike John Mike John Mike John Mike John Mike John Mike");
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }

    @Test
    void testEmptyFirstName() throws Exception {
        patient.setFirstName("");
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }

    @Test
    void testNullFirstName() throws Exception {
        patient.setFirstName(null);
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }

    @Test
    void testFirstNameWithInvalidCharacters() throws Exception {
        patient.setFirstName("john__212");
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }
    //endregion

    //region testing email field constraints

    @Test
    void testEmailLengthGtThan70() throws Exception {
        patient.setEmail("JohnMikeJohnMikeJohnMikeJohnMikeJohnMikeJohnMike@johnmikemailaddress.com");
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }

    @Test
    void testEmptyEmail() throws Exception {
        patient.setEmail("");
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }

    @Test
    void testNullEmail() throws Exception {
        patient.setEmail(null);
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }

    //endregion

    //region testing phone_number field constraints

    @Test
    void testPhoneNumberWithInvalidCharacters() throws Exception {
        patient.setPhoneNumber("0812]#?ab1");
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }

    @Test
    void testPhoneNumberLengthGtThan10() throws Exception {
        patient.setPhoneNumber("081223198711");
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }

    @Test
    void testPhoneNumberLengthLessThan10() throws Exception {
        patient.setPhoneNumber("081223171");
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }

    @Test
    void testEmptyPhoneNumber() throws Exception {
        patient.setPhoneNumber("");
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }

    @Test
    void testNullPhoneNumber() throws Exception {
        patient.setPhoneNumber(null);
        savePatient(objMapper.writeValueAsString(patient), isConflict);
    }

    //endregion

    @Test
    void testUpdate() throws Exception {
        Patient patientPatch = new Patient();
        System.out.println(patientPatch);
        savePatient(objMapper.writeValueAsString(patient), isConflict);

        patientPatch.setEmail("marcus.john@mail.com");

        System.out.println("______________________________________________");
        String url = urlTemplate + "/" + patient.getCnp();
        mvc.perform(MockMvcRequestBuilders.patch(url)
                        .contentType(MediaType.APPLICATION_JSON).content(objMapper.writeValueAsString(patientPatch)))
                .andDo(print());

        System.out.println("______________________________________________");
    }
}
