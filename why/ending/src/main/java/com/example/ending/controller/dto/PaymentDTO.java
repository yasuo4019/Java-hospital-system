package com.example.ending.controller.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PaymentDTO {
    private Integer paymentId;
    private Integer treatmentId;
    private String patientName;
    private BigDecimal amount;
    private Date paymentDate;
    private String paymentMethod;

    // 构造器
    public PaymentDTO() {}

    // Getter 和 Setter 方法
    public Integer getId() {
        return paymentId;
    }
    public void setId(Integer paymentId) {
        this.paymentId = paymentId;
    }


    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }


    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public Date getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }


    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getTreatmentId() {
        return treatmentId;
    }

}
