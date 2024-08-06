package com.example.ending.mapper;

import com.example.ending.controller.dto.PaymentDetailsDTO;
import com.example.ending.entity.Payment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PaymentMapper extends BaseMapper<Payment> {

    @Select("SELECT p.paymentId, pat.name as patientName, p.amount, p.payment_date, p.payment_method "
            + "FROM payments p "
            + "JOIN treatment tr ON p.treatmentId = tr.treatmentId "
            + "JOIN patients pat ON tr.patientId = pat.patientId "
            + "WHERE p.paymentId = #{paymentId}")
    List<PaymentDetailsDTO> getPaymentDetails(Integer paymentId);

    @Select("SELECT p.paymentId, pat.name as patientName, p.amount, p.payment_date, p.payment_method "
            + "FROM payments p "
            + "JOIN treatment tr ON p.treatmentId = tr.treatmentId "
            + "JOIN patients pat ON tr.patientId = pat.patientId")
    List<PaymentDetailsDTO> getAllPaymentDetails();
}