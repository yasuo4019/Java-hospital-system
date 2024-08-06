package com.example.ending.controller.dto;
import lombok.Data;

import java.util.Date;

@Data
public class TreatmentHistoryDetailsDTO {

    private Integer treatmentHistoryId;
    private Integer treatmentId;
    private Integer doctorId;
    private Integer drugId;
    private Integer procedureId;
    private Date recordDate;
    private Date recordTime;
    private String result;
    private String patientName;
    private String doctorName;
    private String drugName;
    private String drugType;
    private String procedureName;
    private String procedureType;
    private String procedureDescription;
}
