package com.example.ending.service;

import com.example.ending.controller.dto.MedicalRecordDetailsDTO;
import com.example.ending.entity.MedicalRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IMedicalRecordService extends IService<MedicalRecord> {

    List<MedicalRecordDetailsDTO> findMedicalRecordDetails();

    Integer findPatientIdByNameAndGender(String patientName, String patientGender);
}
