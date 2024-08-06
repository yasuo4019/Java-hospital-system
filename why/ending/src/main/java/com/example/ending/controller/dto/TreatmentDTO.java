package com.example.ending.controller.dto;

import lombok.Data;
import java.util.Date;

@Data
public class TreatmentDTO {
    private Integer treatmentId;
    private String doctorName;
    private String patientName;
    private String patientBirthday;
    private String departmentName;
    private String diagnosis;
    private Date startDate;
    private Date endDate;

    // 构造器
    public TreatmentDTO() {}
    // Getter 和 Setter 方法
    public Integer getId() {
        return treatmentId;
    }
    public void setId(Integer treatmentId) {
        this.treatmentId = treatmentId;
    }


    public String getDoctorName() {
        return doctorName;
    }
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }


    public String getDepartmentName() {
        return departmentName;
    }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }


    public String getPatientBirthday() {
        return patientBirthday;
    }
    public void setPatientBirthday(String patientBirthday) {
        this.patientBirthday = patientBirthday;
    }

    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }


    public String getDiagnosis() {
        return diagnosis;
    }
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }


    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
