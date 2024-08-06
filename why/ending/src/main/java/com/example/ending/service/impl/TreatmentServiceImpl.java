package com.example.ending.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.ending.entity.Department;
import com.example.ending.entity.Patient;
import com.example.ending.entity.Treatment;
import com.example.ending.mapper.DepartmentMapper;
import com.example.ending.mapper.DoctorMapper;
import com.example.ending.mapper.PatientMapper;
import com.example.ending.mapper.TreatmentMapper;
import com.example.ending.service.ITreatmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.example.ending.controller.dto.TreatmentDetailsDTO;
import java.util.List;
import javax.annotation.Resource;

@Service
public class TreatmentServiceImpl extends ServiceImpl<TreatmentMapper, Treatment> implements ITreatmentService {

    @Resource
    private DoctorMapper doctorMapper;
    @Resource
    private PatientMapper patientMapper;
    @Resource
    private DepartmentMapper departmentMapper;
    @Resource
    private TreatmentMapper treatmentMapper;

    @Override
    public List<TreatmentDetailsDTO> findTreatmentDetails() {
        return treatmentMapper.getTreatmentDetails();
    }

    @Override
    public Integer findDoctorIdByNameAndDepartment(String doctorName, String departmentName) {
        return doctorMapper.findByNameAndDepartment(doctorName, departmentName);
    }

    @Override
    public Integer findPatientIdByName(String patientName) {
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", patientName);
        Patient patient = patientMapper.selectOne(queryWrapper);
        return patient != null ? patient.getId() : null;
    }

    @Override
    public Integer findDepartmentIdByName(String departmentName) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", departmentName);
        Department department = departmentMapper.selectOne(queryWrapper);
        return department != null ? department.getDepartmentId() : null;
    }
}
