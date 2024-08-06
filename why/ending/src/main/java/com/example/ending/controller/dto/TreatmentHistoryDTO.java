package com.example.ending.controller.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TreatmentHistoryDTO {

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



    // treatmentHistoryId
    public Integer getTreatmentHistoryId() {
        return treatmentHistoryId;
    }
    public void setTreatmentHistoryId(Integer treatmentHistoryId) {
        this.treatmentHistoryId = treatmentHistoryId;
    }


    // treatmentId
    public Integer getTreatmentId() {
        return treatmentId;
    }
    public void setTreatmentId(Integer treatmentId) {
        this.treatmentId = treatmentId;
    }


    // doctorId
    public Integer getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }


    // drugId
    public Integer getDrugId() {
        return drugId;
    }
    public void setDrugId(Integer drugId) {
        this.drugId = drugId;
    }


    // procedureId
    public Integer getProcedureId() {
        return procedureId;
    }
    public void setProcedureId(Integer procedureId) {
        this.procedureId = procedureId;
    }


    // recordDate
    public Date getRecordDate() {
        return recordDate;
    }
    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }


    // recordTime
    public Date getRecordTime() {
        return recordTime;
    }
    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }


    // result
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }


    // patientName
    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }


    // doctorName
    public String getDoctorName() {
        return doctorName;
    }
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }


    // drugName
    public String getDrugName() {
        return drugName;
    }
    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }


    // drugType
    public String getDrugType() {
        return drugType;
    }
    public void setDrugType(String drugType) {
        this.drugType = drugType;
    }


    // procedureName
    public String getProcedureName() {
        return procedureName;
    }
    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }


    // procedureType
    public String getProcedureType() {
        return procedureType;
    }
    public void setProcedureType(String procedureType) {
        this.procedureType = procedureType;
    }


    // procedureDescription
    public String getProcedureDescription() {
        return procedureDescription;
    }
    public void setProcedureDescription(String procedureDescription) {
        this.procedureDescription = procedureDescription;
    }

    public Integer getId() {
        return this.treatmentHistoryId;
    }

}
