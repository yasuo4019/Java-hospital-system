package com.example.ending.controller.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class DoctorLocationDTO implements Serializable {
    private Integer doctorId;
    private Integer departmentId;
    private String doctorName;
    private String departmentName;
    private String room;
    private String timeTable;
    private String doctorLevel;
}
