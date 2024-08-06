package com.example.ending.mapper;


import com.example.ending.entity.Treatment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ending.controller.dto.TreatmentDetailsDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TreatmentMapper extends BaseMapper<Treatment> {

    @Select("SELECT t.treatmentId, t.diagnosis, t.start_date as startDate, t.end_date as endDate, "
            + "d.name as doctorName, dp.name as departmentName, p.name as patientName, p.birthday as patientBirthday "
            + "FROM treatment t "
            + "LEFT JOIN doctors d ON t.doctorId = d.doctorId "
            + "LEFT JOIN departments dp ON t.departmentId = dp.departmentId "
            + "LEFT JOIN patients p ON t.patientId = p.patientId")
    List<TreatmentDetailsDTO> getTreatmentDetails();

    @Select("SELECT treatmentId FROM treatment WHERE doctorId = #{doctorId} AND patientId = #{patientId}")
    Integer findTreatmentIdByDoctorIdAndPatientId(@Param("doctorId") Integer doctorId, @Param("patientId") Integer patientId);

}

