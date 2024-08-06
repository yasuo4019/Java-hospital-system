package com.example.ending.controller.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class AppointmentDetailsDTO implements Serializable {
    private Integer appointmentId;
    private String patientName;
    private String doctorName;
    private String room;
    private Date appointmentDate;
    private Date startTime;
    private Date endTime;

}
