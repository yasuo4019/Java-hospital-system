package com.example.ending.mapper;

import com.example.ending.controller.dto.TreatmentHistoryDTO;
import com.example.ending.entity.TreatmentHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ending.controller.dto.TreatmentHistoryDetailsDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TreatmentHistoryMapper extends BaseMapper<TreatmentHistory> {

    @Select("SELECT th.treatmentHistoryId, th.treatmentId, th.doctorId, th.drugId, th.procedureId, th.record_date, th.record_time, th.result, " +
            "p.name as patientName, " +
            "d.name as doctorName, " +
            "ds.name as drugName, ds.type as drugType, " +
            "pr.name as procedureName, pr.type as procedureType, pr.description as procedureDescription " +
            "FROM treatmenthistory th " +
            "LEFT JOIN treatment t ON th.treatmentId = t.treatmentId " +
            "LEFT JOIN patients p ON t.patientId = p.patientId " +
            "LEFT JOIN doctors d ON th.doctorId = d.doctorId " +
            "LEFT JOIN drugsstore ds ON th.drugId = ds.drugId " +
            "LEFT JOIN procedures pr ON th.procedureId = pr.procedureId")
    List<TreatmentHistoryDetailsDTO> getDetailedTreatmentHistory();

    @Select("SELECT " +
            "th.treatmentHistoryId, " +
            "th.treatmentId, " +
            "th.doctorId, " +
            "th.drugId, " +
            "th.procedureId, " +
            "th.record_date AS recordDate, " +
            "th.record_time AS recordTime, " +
            "th.result, " +
            "p.name AS patientName, " +
            "d.name AS doctorName, " +
            "ds.name AS drugName, " +
            "ds.type AS drugType, " +
            "pr.name AS procedureName, " +
            "pr.type AS procedureType, " +
            "pr.description AS procedureDescription " +
            "FROM treatmenthistory th " +
            "LEFT JOIN treatment t ON th.treatmentId = t.treatmentId " +
            "LEFT JOIN patients p ON t.patientId = p.patientId " +
            "LEFT JOIN doctors d ON th.doctorId = d.doctorId " +
            "LEFT JOIN drugsstore ds ON th.drugId = ds.drugId " +
            "LEFT JOIN procedures pr ON th.procedureId = pr.procedureId " +
            "JOIN sys_user su ON p.name = su.username " +
            "WHERE su.username = #{username}")
    List<TreatmentHistoryDetailsDTO> getTreatmentHistoryForUser(@Param("username") String username);



}
