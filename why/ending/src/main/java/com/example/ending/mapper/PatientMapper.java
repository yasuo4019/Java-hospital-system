package com.example.ending.mapper;

import com.example.ending.entity.Patient;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface PatientMapper extends BaseMapper<Patient> {

    // //为treatmenthistory表服务
    @Select("SELECT patientId FROM patients WHERE name = #{name}")
    Integer findPatientIdByName(String name);

    @Select("SELECT * FROM patients WHERE name = #{username}")
    Patient findPatientByUserName(@Param("username") String username);

}
