package com.example.ending.controller.dto;

import lombok.Data;
import java.util.Date;
/**
 * 治疗信息以及相关联的医生、部门和患者详细信息的数据传输对象
 */
@Data
public class TreatmentDetailsDTO {
    private Long treatmentId;
    private String doctorName;
    private String patientName;
    private String departmentName;
    private String patientBirthday;
    private String diagnosis;
    private Date startDate;
    private Date endDate;

}
