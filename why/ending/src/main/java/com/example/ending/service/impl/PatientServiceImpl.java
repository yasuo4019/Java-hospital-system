package com.example.ending.service.impl;

import com.example.ending.entity.Patient;
import com.example.ending.mapper.PatientMapper;
import com.example.ending.service.IPatientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Service Implementation for Doctor
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements IPatientService {

    @Resource
    private PatientMapper patientMapper;

    public Patient getPatientByUsername(String username) {
        return patientMapper.findPatientByUserName(username);
    }
}

