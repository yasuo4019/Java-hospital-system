package com.example.ending.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ending.common.Result;
import com.example.ending.controller.dto.MedicalRecordDTO;
import com.example.ending.controller.dto.MedicalRecordDetailsDTO;
import com.example.ending.entity.MedicalRecord;
import com.example.ending.service.IMedicalRecordService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

/**
 * <p>
 * 前端控制器 for MedicalRecord
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
@RestController
@RequestMapping("/medicalRecords")
public class MedicalRecordController {

    @Resource
    private IMedicalRecordService medicalRecordService;

    private MedicalRecord convertDtoToEntity(MedicalRecordDTO medicalRecordDto) {
        Integer patientId = medicalRecordService.findPatientIdByNameAndGender(
                medicalRecordDto.getPatientName(),
                medicalRecordDto.getPatientGender()
        );

        if (patientId == null) {
            throw new RuntimeException("Patient information cannot be recognized or does not match");
            //患者信息无法识别或不匹配
        }

        MedicalRecord medicalRecord = new MedicalRecord();
        if (medicalRecordDto.getMedicalRecordId() != null) {
            medicalRecord.setMedicalRecordId(medicalRecordDto.getMedicalRecordId());
        }
        medicalRecord.setPatientId(patientId);
        medicalRecord.setRecordDate(medicalRecordDto.getRecordDate());
        medicalRecord.setRecordText(medicalRecordDto.getRecordText());

        return medicalRecord;
    }

    @PostMapping
    public Result saveOrUpdateMedicalRecord(@RequestBody MedicalRecordDTO medicalRecordDto) {
        MedicalRecord medicalRecord = convertDtoToEntity(medicalRecordDto);
        boolean success;

        if (medicalRecord.getMedicalRecordId() != null && medicalRecordService.getById(medicalRecord.getMedicalRecordId()) != null) {
            success = medicalRecordService.updateById(medicalRecord);
        } else {
            success = medicalRecordService.save(medicalRecord);
        }

        if (success) {
            MedicalRecord updatedMedicalRecord = medicalRecordService.getById(medicalRecord.getMedicalRecordId());
            return Result.success(updatedMedicalRecord);
        } else {
            return Result.failure();
        }
    }

    // 删除医疗记录
    @DeleteMapping("/{medicalRecordId}")
    public Result deleteMedicalRecord(@PathVariable Integer medicalRecordId) {
        return Result.success(medicalRecordService.removeById(medicalRecordId));
    }

    // 批量删除医疗记录
    @PostMapping("/del/batch")
    public Result deleteMedicalRecordBatch(@RequestBody List<Integer> medicalRecordIds) {
        return Result.success(medicalRecordService.removeByIds(medicalRecordIds));
    }

    // 查找所有医疗记录
    @GetMapping
    public Result findAllMedicalRecords() {
        return Result.success(medicalRecordService.list());
    }

    // 根据医疗记录ID查找记录
    @GetMapping("/{medicalRecordId}")
    public Result findMedicalRecordById(@PathVariable Integer medicalRecordId) {
        return Result.success(medicalRecordService.getById(medicalRecordId));
    }

    // 获取治疗详情，包括医生、部门和患者的信息
    @GetMapping("/details")
    public Result getMedicalRecordDetails() {
        List<MedicalRecordDetailsDTO> details = medicalRecordService.findMedicalRecordDetails();
        return Result.success(details);
    }

    // 分页查询医疗记录
    @GetMapping("/page")
    public Result findMedicalRecordPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam(defaultValue = "") String keyword) {
        QueryWrapper<MedicalRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("medicalRecordId");
        if (!"".equals(keyword)) {
            queryWrapper.like("recordText", keyword);
        }
        return Result.success(medicalRecordService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}
