package com.example.ending.service.impl;

import com.example.ending.entity.Payment;
import com.example.ending.mapper.PaymentMapper;
import com.example.ending.service.IPaymentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.example.ending.controller.dto.PaymentDetailsDTO;

import java.util.List;

@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements IPaymentService {

    @Resource
    private PaymentMapper paymentMapper;

    @Override
    public PaymentDetailsDTO getPaymentDetailsById(Integer paymentId) {
        List<PaymentDetailsDTO> paymentDetailsList = paymentMapper.getPaymentDetails(paymentId);
        if (!paymentDetailsList.isEmpty()) {
            return paymentDetailsList.get(0);
        }
        return null;
    }
//
    @Override
    public List<PaymentDetailsDTO> findPaymentDetails() {
        return paymentMapper.getAllPaymentDetails();
    }
}