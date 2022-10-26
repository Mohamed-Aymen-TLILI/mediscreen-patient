package com.mediscreen.patient.service;

import com.mediscreen.patient.PatientApplication;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.repository.PatientRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes={ PatientApplication.class })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatientServiceTest {

    private Patient patient = new Patient();
    private final Patient patientToDelete = new Patient();
    private Long idToDelete = 0L;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    @BeforeAll
    public void init() throws ParseException {
        String birthdateStr = "1991-06-24";
        LocalDate birthday = LocalDate.parse(birthdateStr);

        patient.setId(3L);
        patient.setFirstName("test");
        patient.setLastName("TestTest");
        patient.setBirthDate(birthday);
        patient.setGender("F");
        patientService.savePatient(patient);
    }

    @AfterAll
    public void clean() {
        patientRepository.delete(patient);
    }

    @Test
    @Order(1)
    public void savePatientTest() {
        patient = patientRepository.save(patient);
        Assertions.assertNotNull(patient.getId());
        assertEquals(patient.getLastName(), "TestTest");
    }

    @Test
    @Order(2)
    public void findPatientByIdTest(){
        Long id = patient.getId();
        patientService.findPatientById(id);
        assertEquals("test", patient.getFirstName());
        assertEquals("TestTest", patient.getLastName());
        assertEquals("F", patient.getGender());
    }

    @Test
    @Order(3)
    public void findPatientByLastNameTest(){
        String lastName = "TestNone";
        String firstName = "Test";
        List<Patient> patientList = patientService.findPatientByLastNameAndFirstName(lastName, firstName);
        assertTrue(patientList.size() > 0);
    }

    @Test
    @Order(4)
    public void updatePatientTest() {
        Long id = patient.getId();
        Patient patientById = patientService.findPatientById(id);
        patientById.setLastName("TestTest");
        patientById.setFirstName("test");
        Patient updatedPatient = patientService.updatePatient(id, patientById);
        assertEquals("TestTest", updatedPatient.getLastName());
        assertEquals("test", updatedPatient.getFirstName());

    }

    @Test
    @Order(5)
    public void findAllPatientsTest() {
        List<Patient> patientList = patientService.findAllPatients();
        assertTrue(patientList.size() > 0);
    }

    @Test
    @Order(6)
    public void deletePatientByIdTest() {
        Long idPatientToDelete = patient.getId();
        patientService.deletePatientById(idPatientToDelete);
        Patient patientById = patientService.findPatientById(idPatientToDelete);
        assertNull(patientById);
    }

    @Test
    @Order(7)
    public void updatePatientReturnNullTest() {
        Patient patient = patientService.updatePatient(idToDelete, patientToDelete);
        assertNull(patient);
    }

}
