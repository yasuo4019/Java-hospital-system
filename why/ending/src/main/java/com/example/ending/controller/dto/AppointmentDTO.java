package com.example.ending.controller.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AppointmentDTO implements Serializable {
    private Integer appointmentId;
    private Integer patientId;
    private Integer doctorId;
    private String doctorName; // 添加医生姓名字段
    private String patientName; // 添加患者姓名字段
    private Date appointmentDate;
    private Date startTime;
    private Date endTime;
    private String room;

    // 构造器
    public AppointmentDTO() {}

    // Getter 和 Setter 方法由 Lombok 自动生成

    // 以下为额外的 Getter 和 Setter 方法，如果您需要手动添加
    public Integer getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Integer getPatientId() {
        return patientId;
    }
    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }
    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getRoom() {
        return room;
    }
    public void setRoom(String room) {
        this.room = room;
    }
}
