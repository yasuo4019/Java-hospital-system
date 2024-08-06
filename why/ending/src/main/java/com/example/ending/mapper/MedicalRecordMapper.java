package com.example.ending.mapper;

import com.example.ending.entity.MedicalRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ending.controller.dto.MedicalRecordDetailsDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MedicalRecordMapper extends BaseMapper<MedicalRecord> {

    @Select("SELECT mr.medicalrecordId, mr.record_date as recordDate, mr.record_text as recordText, "
            + "p.name as patientName, p.gender as patientGender "
            + "FROM medicalrecords mr "
            + "LEFT JOIN patients p ON mr.patientId = p.patientId")
    List<MedicalRecordDetailsDTO> getMedicalRecordDetails();

    @Select("SELECT patientId FROM patients WHERE name = #{name} AND gender = #{gender}")
    Integer findPatientIdByNameAndGender(@Param("name") String name, @Param("gender") String gender);

}
