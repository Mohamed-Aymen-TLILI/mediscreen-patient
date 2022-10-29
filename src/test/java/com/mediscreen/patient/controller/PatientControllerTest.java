package com.mediscreen.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.patient.PatientApplication;
import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.service.PatientService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK, classes={ PatientApplication.class })
public class PatientControllerTest {

    private Patient patient = new Patient();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private PatientService mockPatientService;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public void setUp(){
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void savePatientTest() throws Exception {
        String birthdateStr = "1945-06-24";
        LocalDate birthdate = LocalDate.parse(birthdateStr);

        patient.setId(159L);
        patient.setFirstName("Aymen");
        patient.setLastName("TLILI");
        patient.setBirthDate(birthdate);
        patient.setGender("M");
        patient.setAdress("");
        patient.setPhone("");

        when(mockPatientService.savePatient(any(Patient.class))).thenReturn(patient);
        mockMvc.perform(post("/api/patient")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
               /* .andExpect(status().isCreated());
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("bbb"));*/

    }

    @Test
    public void findPatientByIdTest() throws Exception {
        String birthDateStr = "1945-06-24";
        LocalDate birthDate = LocalDate.parse(birthDateStr);
        Patient patient = new Patient();
        patient.setId(200L);
        patient.setLastName("aaaaa");
        patient.setFirstName("bbb");
        patient.setBirthDate(birthDate);
        patient.setAdress("2 High St");
        patient.setGender("F");
        patient.setPhone("200-333-4444");

        when(mockPatientService.findPatientById(200L)).thenReturn(patient);
        mockMvc.perform(get("/api/patients/patient/200")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("bbb"));
    }

    @Test
    public void findPatientByIdBadRequestTest() throws Exception {
        when(mockPatientService.findPatientById(81L)).thenReturn(null);
        mockMvc.perform(get("/api/patients/patient/41"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findPatientByLastNameTest() throws Exception {
        String birthDateStr = "1945-06-24";
        LocalDate birthDate = LocalDate.parse(birthDateStr);
        Patient patient1 = new Patient();
        patient1.setId(52L);
        patient1.setFirstName("Badis");
        patient1.setLastName("BenDriss");
        patient1.setBirthDate(birthDate);
        patient1.setAdress("2 Rue St");
        patient1.setGender("M");
        patient1.setPhone("600-555-888");

        Patient patient2 = new Patient();
        patient2.setId(34L);
        patient2.setFirstName("Alex");
        patient2.setLastName("Théo");
        patient2.setBirthDate(birthDate);
        patient2.setAdress("2 Rue St");
        patient2.setGender("M");
        patient2.setPhone("400-111-999");

        List<Patient> patientList = new ArrayList<>();
        patientList.add(patient1);
        patientList.add(patient2);

        when(mockPatientService.findPatientByLastNameAndFirstName("BenDriss", "Badis")).thenReturn(patientList);
        mockMvc.perform(get("/api/patients/patient/family")
                        .param("lastName", "Bertrand")
                        .param("firstName", "Batis")
                        .contentType(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$..*").isNotEmpty())
                .andExpect(jsonPath("$.[0].firstName").value("Batis"))
                .andExpect(jsonPath("$.[1].firstName").value("Alex"));
    }

    @Test
    public void findListPatientIsEmpty() throws Exception {
        when(mockPatientService.findPatientByLastNameAndFirstName("ddd", "ccccccc")).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/patients/patient/family")
                        .param("lastName", "ddd")
                        .param("firstName", "ccccccc")
                        .contentType(MediaType.ALL))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updatePatientTest() throws Exception {
        String birthDateStr = "2000-06-24";
        LocalDate birthDate = LocalDate.parse(birthDateStr);
        Patient updatePatient = new Patient();
        updatePatient.setId(4L);
        updatePatient.setFirstName("TLILI");
        updatePatient.setLastName("Badis");
        updatePatient.setBirthDate(birthDate);
        updatePatient.setAdress("2 Avenue St");
        updatePatient.setGender("M");
        updatePatient.setPhone("5111-222-6666");

        when(mockPatientService.updatePatient(4L, updatePatient)).thenReturn(updatePatient);
        mockMvc.perform(put("/api/patient/4")
                .contentType(MediaType.APPLICATION_JSON)
                // .content(asJsonString(updatePatient))
                .accept(MediaType.APPLICATION_JSON));
        // .andExpect(status().isOk());
    }

    @Test
    public void updatePatientBindingResultWhenInputIsNull() throws Exception {
        String birthDateStr = "1968-10-29";
        LocalDate birthDate = LocalDate.parse(birthDateStr);
        Patient updatedPatient = new Patient();
        updatedPatient.setId(33L);
        updatedPatient.setFirstName("TLILI");
        updatedPatient.setLastName("Badis");
        updatedPatient.setBirthDate(birthDate);
        updatedPatient.setAdress("2 Avenue Sttttttttt");
        updatedPatient.setGender("M");
        updatedPatient.setPhone("5111-222-6666");

        when(mockPatientService.updatePatient(33L, updatedPatient)).thenReturn(updatedPatient);
        mockMvc.perform(put("/api/patient/33")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updatePatientBadRequestTest() throws Exception {
        String birthDateStr = "1945-06-24";
        LocalDate birthDate = LocalDate.parse(birthDateStr);
        Patient updatePatient = new Patient();
        updatePatient.setId(0L);
        updatePatient.setLastName("Boole");
        updatePatient.setFirstName("Clarisse");
        updatePatient.setBirthDate(birthDate);
        updatePatient.setAdress("9 Avenue St");
        updatePatient.setGender("F");
        updatePatient.setPhone("5111-222-6666");

        when(mockPatientService.updatePatient(0L, updatePatient)).thenReturn(updatePatient);
        mockMvc.perform(put("/api/patient/0")
                .contentType(MediaType.APPLICATION_JSON));
        //.andExpect(status().isBadRequest());
    }

    @Test
    public void findAllPatientsTest() throws Exception {
        String birthDateStr = "1945-06-24";
        LocalDate birthDate = LocalDate.parse(birthDateStr);
        Patient patient1 = new Patient();
        patient1.setId(52L);
        patient1.setFirstName("Batis");
        patient1.setLastName("Bertrand");
        patient1.setBirthDate(birthDate);
        patient1.setAdress("2 Rue St");
        patient1.setGender("F");
        patient1.setPhone("600-555-888");

        Patient patient2 = new Patient();
        patient2.setId(34L);
        patient2.setFirstName("Badis");
        patient2.setLastName("BenDriss");
        patient2.setBirthDate(birthDate);
        patient2.setAdress("2 Rue St");
        patient2.setGender("M");
        patient2.setPhone("400-111-999");

        List<Patient> patientList = new ArrayList<>();
        patientList.add(patient1);
        patientList.add(patient2);

        when(mockPatientService.findAllPatients()).thenReturn(patientList);
        mockMvc.perform(get("/api/patients")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$..*").isNotEmpty())
                .andExpect(jsonPath("$.[0].firstName").value("Batis"))
                .andExpect(jsonPath("$.[1].firstName").value("Alex"));
    }

    @Test
    public void deletePatientByIdTest() throws Exception {
        when(mockPatientService.deletePatientById(0L)).thenReturn(true);
        mockMvc.perform(delete("/api/patients/patientId/0")
                        .contentType(MediaType.ALL))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePatientByIdReturn404() throws Exception {
        when(mockPatientService.deletePatientById(1111L)).thenReturn(false);
        mockMvc.perform(delete("/api/patients/patientId/1111")
        ).andExpect(status().isNotFound());
    }

    @Test
    public void findPatientByIdNotFound() throws Exception {
        when(mockPatientService.findPatientById(1L)).thenReturn(null);
        mockMvc.perform(get("/api/patients/patient/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findPatientByLastNameStatusNull() throws Exception {
        when(mockPatientService.findPatientByLastNameAndFirstName("fff", "eeeee")).thenReturn(null);
        mockMvc.perform(get("/api/patients")
                        .param("lastName", "fff")
                        .contentType(MediaType.ALL))
                .andExpect(status().isNoContent());
    }


    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Date parseDate(String myDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(myDate);
        long timeInMillis = date.getTime();
        return new Date(timeInMillis);
    }

}
