package com.example.ending.service;

import com.example.ending.entity.Treatment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ending.controller.dto.TreatmentDetailsDTO;
import java.util.List;

public interface ITreatmentService extends IService<Treatment> {
    List<TreatmentDetailsDTO> findTreatmentDetails();
    Integer findDoctorIdByNameAndDepartment(String doctorName, String departmentName);
    Integer findPatientIdByName(String patientName);
    Integer findDepartmentIdByName(String departmentName);

}
