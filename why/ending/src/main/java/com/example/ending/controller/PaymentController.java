package com.example.ending.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ending.common.Result;
import com.example.ending.controller.dto.PaymentDTO;
import com.example.ending.controller.dto.PaymentDetailsDTO;
import com.example.ending.entity.Payment;
import com.example.ending.service.IPaymentService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

/**
 * <p>
 * 前端控制器 for Payment
 * </p>
 *
 * @since 2023-05-06
 */
@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Resource
    private IPaymentService paymentService;

    private Payment convertDtoToEntity(PaymentDTO paymentDto) {
        Payment payment = new Payment();
        payment.setPaymentId(paymentDto.getId());
        payment.setTreatmentId(paymentDto.getTreatmentId()); // 确保设置了 treatmentId
        payment.setAmount(paymentDto.getAmount());
        payment.setPaymentDate(paymentDto.getPaymentDate());
        payment.setPaymentMethod(paymentDto.getPaymentMethod());
        return payment;
    }
    // 新增或者更新支付信息
    @PostMapping
    public Result saveOrUpdatePayment(@RequestBody PaymentDTO paymentDto) {
        Payment payment = convertDtoToEntity(paymentDto);
        boolean success = paymentService.saveOrUpdate(payment);
        if (success) {
            PaymentDetailsDTO paymentDetails = paymentService.getPaymentDetailsById(payment.getPaymentId());
            return Result.success(paymentDetails);
        } else {
            return Result.failure();
        }
    }


    // 删除支付信息
    @DeleteMapping("/{paymentId}")
    public Result deletePayment(@PathVariable Integer paymentId) {
        return Result.success(paymentService.removeById(paymentId));
    }

    // 批量删除支付信息
    @PostMapping("/del/batch")
    public Result deletePaymentBatch(@RequestBody List<Integer> paymentIds) {
        return Result.success(paymentService.removeByIds(paymentIds));
    }

    // 查找所有支付信息
    @GetMapping
    public Result findAllPayments() {
        return Result.success(paymentService.list());
    }

    // 根据支付ID查找支付信息
    @GetMapping("/{paymentId}")
    public Result findPaymentById(@PathVariable Integer paymentId) {
        return Result.success(paymentService.getById(paymentId));
    }


    //
    // 获取支付详情，可能包括患者、治疗等相关信息
    @GetMapping("/details")
    public Result getPaymentDetails() {
        List<PaymentDetailsDTO> details = paymentService.findPaymentDetails();
        return Result.success(details);
    }

    // 分页查询支付信息
    @GetMapping("/page")
    public Result findTreatmentPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(defaultValue = "") String amount) {
        QueryWrapper<Payment> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("paymentId");
        if (!"".equals(amount)) {
            queryWrapper.like("amount", amount);
        }
        return Result.success(paymentService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}
