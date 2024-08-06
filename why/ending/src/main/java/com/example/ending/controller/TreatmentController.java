package com.example.ending.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ending.common.Result;
import com.example.ending.controller.dto.TreatmentDTO;
import com.example.ending.controller.dto.TreatmentDetailsDTO;
import com.example.ending.entity.Treatment;
import com.example.ending.service.ITreatmentService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

/**
 * <p>
 * 前端控制器 for Treatment
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
@RestController
@RequestMapping("/treatments")
public class TreatmentController {

    @Resource
    private ITreatmentService treatmentService;

    private Treatment convertDtoToEntity(TreatmentDTO treatmentDto) {
        Integer doctorId = treatmentService.findDoctorIdByNameAndDepartment(
                treatmentDto.getDoctorName(),
                treatmentDto.getDepartmentName()
        );
        Integer patientId = treatmentService.findPatientIdByName(treatmentDto.getPatientName());
        Integer departmentId = treatmentService.findDepartmentIdByName(treatmentDto.getDepartmentName());

        if (doctorId == null || patientId == null) {
            throw new RuntimeException("Doctor or patient information cannot be identified");
        }

        Treatment treatment = new Treatment();
        if (treatmentDto.getId() != null) {
            treatment.setId(treatmentDto.getId());
        }
        treatment.setDoctorId(doctorId);
        treatment.setPatientId(patientId);
        treatment.setDepartmentId(departmentId);
        treatment.setDiagnosis(treatmentDto.getDiagnosis());
        treatment.setStartDate(treatmentDto.getStartDate());
        treatment.setEndDate(treatmentDto.getEndDate());
        return treatment;
    }

    @PostMapping
    public Result saveOrUpdateTreatment(@RequestBody TreatmentDTO treatmentDto) {
        Treatment treatment = convertDtoToEntity(treatmentDto);
        boolean success;

        // 检查数据库中是否存在对应ID的记录
        if (treatment.getId() != null && treatmentService.getById(treatment.getId()) != null) {
            // 如果存在，执行更新
            success = treatmentService.updateById(treatment);
        } else {
            // 如果不存在或者没有提供ID，执行插入
            success = treatmentService.save(treatment);
        }
        // 如果操作成功，返回更新后的实体或确认信息
        if (success) {
            Treatment updatedTreatment = treatmentService.getById(treatment.getId());
            return Result.success(updatedTreatment);
        } else {
            return Result.failure();
        }
    }


    // 删除治疗信息
    @DeleteMapping("/{treatmentId}")
    public Result deleteTreatment(@PathVariable Integer treatmentId) {
        return Result.success(treatmentService.removeById(treatmentId));
    }

    // 批量删除治疗信息
    @PostMapping("/del/batch")
    public Result deleteTreatmentBatch(@RequestBody List<Integer> treatmentIds) {
        return Result.success(treatmentService.removeByIds(treatmentIds));
    }

    // 查找所有治疗信息
    @GetMapping
    public Result findAllTreatments() {
        return Result.success(treatmentService.list());
    }

    // 根据治疗ID查找治疗信息
    @GetMapping("/{treatmentId}")
    public Result findTreatmentById(@PathVariable Integer treatmentId) {
        return Result.success(treatmentService.getById(treatmentId));
    }

    // 获取治疗详情，包括医生、部门和患者的信息
    @GetMapping("/details")
    public Result getTreatmentDetails() {
        List<TreatmentDetailsDTO> details = treatmentService.findTreatmentDetails();
        return Result.success(details);
    }

    // 分页查询治疗信息
    @GetMapping("/page")
    public Result findTreatmentPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(defaultValue = "") String diagnosis) {
        QueryWrapper<Treatment> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("treatmentId");
        if (!"".equals(diagnosis)) {
            queryWrapper.like("diagnosis", diagnosis);
        }
        return Result.success(treatmentService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}
