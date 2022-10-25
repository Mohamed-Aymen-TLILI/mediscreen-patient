package com.mediscreen.patient.service;

import com.mediscreen.patient.model.Patient;
import com.mediscreen.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService{

    @Autowired
    PatientRepository patientRepository;


    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient findPatientById(Long id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        return patientOptional.orElse(null);
    }

    public List<Patient> findPatientByLastNameAndFirstName(String lastName, String firstName) {
        return patientRepository.findPatientByLastNameAndFirstName(lastName, firstName);
    }

    public Patient updatePatient(Long id, Patient patient) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (patientOptional.isEmpty()){
            return null;
        }
        Patient updatePatient = patientOptional.get();
        updatePatient.setLastName(patient.getLastName());
        updatePatient.setFirstName(patient.getFirstName());
        updatePatient.setBirthDate(patient.getBirthDate());
        updatePatient.setAdress(patient.getAdress());
        updatePatient.setGender(patient.getGender());
        updatePatient.setPhone(patient.getPhone());
        patientRepository.save(updatePatient);
        return updatePatient;
    }

    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
    }

    public Boolean deletePatientById(Long id) {
        boolean deletePatient = false;
        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (patientOptional.isPresent()){
            patientRepository.deleteById(id);
            deletePatient = true;
        }
        return deletePatient;
    }
}