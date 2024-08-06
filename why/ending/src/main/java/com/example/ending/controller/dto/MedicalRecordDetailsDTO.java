package com.example.ending.controller.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MedicalRecordDetailsDTO {
    private Integer medicalRecordId;
    private String patientName; // Name from the patients table
    private String patientGender; // Gender from the patients table
    private Date recordDate;
    private String recordText;

}
