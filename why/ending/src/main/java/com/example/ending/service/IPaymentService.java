package com.example.ending.service;

import com.example.ending.controller.dto.PaymentDetailsDTO;
import com.example.ending.entity.Payment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务接口 for Payment
 * </p>
 */
public interface IPaymentService extends IService<Payment> {

    PaymentDetailsDTO getPaymentDetailsById(Integer paymentId);

    //
    List<PaymentDetailsDTO> findPaymentDetails();
    //Integer findPatientIdByName(String patientName);
}
