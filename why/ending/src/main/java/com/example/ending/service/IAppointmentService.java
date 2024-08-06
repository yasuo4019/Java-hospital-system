package com.example.ending.service;

import com.example.ending.controller.dto.AppointmentDetailsDTO;
import com.example.ending.entity.Appointment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务接口 for Appointment
 * </p>
 */
public interface IAppointmentService extends IService<Appointment> {

    AppointmentDetailsDTO getAppointmentDetailsById(Integer appointmentId);

    List<AppointmentDetailsDTO> findAllAppointmentDetails();

    String findRoomByDoctorId(Integer doctorId);

    // 根据医生的姓名查找医生的ID
    Integer findDoctorIdByName(String doctorName);

    // 根据患者的姓名查找患者的ID
    Integer findPatientIdByName(String patientName);

    //为行级安全服务
    List<AppointmentDetailsDTO> getAppointmentsForUser(String username);
}
