package com.example.ending.service.impl;

import com.example.ending.controller.dto.AppointmentDetailsDTO;
import com.example.ending.entity.Appointment;
import com.example.ending.mapper.AppointmentMapper;
import com.example.ending.service.IAppointmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements IAppointmentService {

    @Resource
    private AppointmentMapper appointmentMapper;

    @Override
    public AppointmentDetailsDTO getAppointmentDetailsById(Integer appointmentId) {
        List<AppointmentDetailsDTO> appointmentDetailsList = appointmentMapper.getAppointmentDetails(appointmentId);
        if (!appointmentDetailsList.isEmpty()) {
            return appointmentDetailsList.get(0);
        }
        return null;
    }

    @Override
    public List<AppointmentDetailsDTO> findAllAppointmentDetails() {
        return appointmentMapper.getAllAppointmentDetails();
    }

    @Override
    public String findRoomByDoctorId(Integer doctorId) {
        return appointmentMapper.findRoomByDoctorId(doctorId);
    }

    @Override
    public Integer findDoctorIdByName(String doctorName) {
        return appointmentMapper.findDoctorIdByName(doctorName);
    }

    @Override
    public Integer findPatientIdByName(String patientName) {
        return appointmentMapper.findPatientIdByName(patientName);
    }

    //为行级安全服务
    public List<AppointmentDetailsDTO> getAppointmentsForUser(String username) {
        return appointmentMapper.getAppointmentsForUser(username);
    }

}
