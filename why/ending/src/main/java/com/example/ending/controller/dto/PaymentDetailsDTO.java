package com.example.ending.controller.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付信息以及相关联的治疗和患者详细信息的数据传输对象
 */
@Data
public class PaymentDetailsDTO {
    private Integer paymentId;
    private Integer treatmentId; // 添加的字段
    private String patientName;
    private BigDecimal amount;
    private Date paymentDate;
    private String paymentMethod;
}