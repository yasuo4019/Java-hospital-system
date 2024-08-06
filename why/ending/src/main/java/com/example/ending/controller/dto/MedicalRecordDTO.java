package com.example.ending.controller.dto;

import lombok.Data;
import java.util.Date;

@Data
public class MedicalRecordDTO {
    private Integer medicalRecordId;
    private String patientName; // Assuming you want the name instead of ID for user-friendliness
    private String patientGender; // Gender of the patient
    private Date recordDate;
    private String recordText;

    // Constructors
    public MedicalRecordDTO() {}

    // Getters and Setters
    public Integer getMedicalRecordId() {
        return medicalRecordId;
    }
    public void setMedicalRecordId(Integer medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }


    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }


    public Date getRecordDate() {
        return recordDate;
    }
    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }


    public String getRecordText() {
        return recordText;
    }
    public void setRecordText(String recordText) {
        this.recordText = recordText;
    }


    public String getPatientGender() {
        return patientGender;
    }
    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }
}
