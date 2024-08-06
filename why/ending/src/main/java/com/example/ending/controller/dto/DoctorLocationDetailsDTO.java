package com.example.ending.controller.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class DoctorLocationDetailsDTO implements Serializable {
    private Integer doctorId;
    private String doctorName;
    private Integer departmentId;
    private String departmentName;
    private String room;
    private String timeTable;
    private String doctorLevel;
}
