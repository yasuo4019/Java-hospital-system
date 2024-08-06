package com.example.ending.mapper;

import com.example.ending.controller.dto.AppointmentDetailsDTO;
import com.example.ending.entity.Appointment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {

    @Select("SELECT a.appointmentId, pat.name as patientName, doc.name as doctorName, dl.room, " +
            "a.appointment_date, a.start_time, a.end_time " +
            "FROM appointments a " +
            "JOIN patients pat ON a.patientId = pat.patientId " +
            "JOIN doctors doc ON a.doctorId = doc.doctorId " +
            "JOIN doctorlocation dl ON doc.doctorId = dl.doctorId")
    List<AppointmentDetailsDTO> getAllAppointmentDetails();

    // 新增查询：根据预约ID获取单个预约详情
    @Select("SELECT a.appointmentId, pat.name as patientName, doc.name as doctorName, dl.room, " +
            "a.appointment_date, a.start_time, a.end_time " +
            "FROM appointments a " +
            "JOIN patients pat ON a.patientId = pat.patientId " +
            "JOIN doctors doc ON a.doctorId = doc.doctorId " +
            "JOIN doctorlocation dl ON doc.doctorId = dl.doctorId " +
            "WHERE a.appointmentId = #{appointmentId}")
    List<AppointmentDetailsDTO> getAppointmentDetails(Integer appointmentId);

    @Select("SELECT room FROM doctorlocation WHERE doctorId = #{doctorId}")
    String findRoomByDoctorId(Integer doctorId);

    @Select("SELECT doctorId FROM doctors WHERE name = #{doctorName}")
    Integer findDoctorIdByName(String doctorName);

    @Select("SELECT patientId FROM patients WHERE name = #{patientName}")
    Integer findPatientIdByName(String patientName);


    //为行级安全服务
    @Select("SELECT a.appointmentId, pat.name as patientName, doc.name as doctorName, dl.room, " +
            "a.appointment_date, a.start_time, a.end_time " +
            "FROM appointments a " +
            "JOIN patients pat ON a.patientId = pat.patientId " +
            "JOIN doctors doc ON a.doctorId = doc.doctorId " +
            "JOIN doctorlocation dl ON doc.doctorId = dl.doctorId " +
            "JOIN sys_user su ON pat.name = su.username " +
            "WHERE su.username = #{username}")
    List<AppointmentDetailsDTO> getAppointmentsForUser(@Param("username") String username);


}
