package com.example.ending.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ending.controller.dto.MedicalRecordDetailsDTO;
import com.example.ending.entity.MedicalRecord;
import com.example.ending.mapper.MedicalRecordMapper;
import com.example.ending.service.IMedicalRecordService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class MedicalRecordServiceImpl extends ServiceImpl<MedicalRecordMapper, MedicalRecord> implements IMedicalRecordService {

    @Resource
    private MedicalRecordMapper MedicalRecordMapper;

    @Override
    public List<MedicalRecordDetailsDTO> findMedicalRecordDetails() {
        return MedicalRecordMapper.getMedicalRecordDetails();
    }


    public Integer findPatientIdByNameAndGender(String name, String gender) {
        return MedicalRecordMapper.findPatientIdByNameAndGender(name, gender);
    }

}
