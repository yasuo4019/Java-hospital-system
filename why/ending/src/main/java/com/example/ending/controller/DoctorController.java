package com.example.ending.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ending.common.Result;
import com.example.ending.entity.Doctor;
import com.example.ending.service.IDoctorService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

/**
 * <p>
 * 前端控制器 for Doctors
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Resource
    private IDoctorService doctorService;

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    // 新增或者更新医生信息
    @PostMapping
    public Result saveOrUpdateDoctor(@RequestBody Doctor doctor) {
        return Result.success(doctorService.saveOrUpdate(doctor));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    // 删除医生信息
    @DeleteMapping("/{doctorId}")
    public Result deleteDoctor(@PathVariable Integer doctorId) {
        return Result.success(doctorService.removeById(doctorId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    // 批量删除医生信息
    @PostMapping("/del/batch")
    public Result deleteDoctorBatch(@RequestBody List<Integer> doctorIds) {
        return Result.success(doctorService.removeByIds(doctorIds));
    }

    //@PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    // 查找所有医生信息
    @GetMapping
    public Result findAllDoctors() {
        return Result.success(doctorService.list());
    }

    //@PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    // 根据医生ID查找医生
    @GetMapping("/{doctorId}")
    public Result findDoctorById(@PathVariable Integer doctorId) {
        return Result.success(doctorService.getById(doctorId));
    }

    //@PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    // 分页查询医生信息
    @GetMapping("/page")
    public Result findDoctorPage(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam(defaultValue = "") String name,
                                 @RequestParam(defaultValue = "") String specialization) {
        QueryWrapper<Doctor> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("doctorId");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        if (!"".equals(specialization)) {
            queryWrapper.like("specialization", specialization);
        }
        return Result.success(doctorService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

