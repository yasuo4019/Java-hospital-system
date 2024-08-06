package com.example.ending.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ending.common.Result;
import com.example.ending.entity.Patient;
import com.example.ending.service.IPatientService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 前端控制器 for Patient
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
@RestController
@RequestMapping("/patients")
public class PatientController {

    @Resource
    private IPatientService patientService;

    // 新增或者更新病人信息
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public Result saveOrUpdatePatient(@RequestBody Patient patient) {
        return Result.success(patientService.saveOrUpdate(patient));
    }

    // 删除病人信息
    @DeleteMapping("/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public Result deletePatient(@PathVariable Integer patientId) {
        return Result.success(patientService.removeById(patientId));
    }

    // 批量删除病人信息
    @PostMapping("/del/batch")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public Result deletePatientBatch(@RequestBody List<Integer> patientIds) {
        return Result.success(patientService.removeByIds(patientIds));
    }

     //查找所有病人信息
    @GetMapping
    public Result findAllPatients() {
        return Result.success(patientService.list());
    }


    // 根据病人ID查找病人
    @GetMapping("/{patientId}")
    public Result findPatientById(@PathVariable Integer patientId) {
        return Result.success(patientService.getById(patientId));
    }

    @GetMapping("/page")
    public Result findPatientPage(@RequestParam Integer pageNum,
                                  @RequestParam Integer pageSize,
                                  @RequestParam(defaultValue = "") String name,
                                  @RequestParam(defaultValue = "") String gender,
                                  Authentication authentication) {
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("patientId");

        // 获取当前用户的角色
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isUserRole = authorities.contains(new SimpleGrantedAuthority("ROLE_USER"));

        // 如果用户角色是ROLE_USER
        if (isUserRole) {
            Object principal = authentication.getPrincipal();
            String currentUserName = null;

            if (principal instanceof com.example.ending.entity.User) {
                com.example.ending.entity.User customUser = (com.example.ending.entity.User) principal;
                currentUserName = customUser.getUsername();
            }

            if (currentUserName != null) {
                Patient patient = patientService.getPatientByUsername(currentUserName);
                if (patient != null) {
                    queryWrapper.eq("name", patient.getName());
                } else {
                    // 如果ROLE_USER用户没有匹配的病人，返回空页
                    return Result.success(new Page<Patient>());
                }
            } else {
                // 如果无法从principal获取用户名，也返回空页
                return Result.success(new Page<Patient>());
            }
        }

        // 对于其他角色，应用常规的过滤条件
        if (!isUserRole) {
            if (!name.isEmpty()) {
                queryWrapper.like("name", name);
            }
            if (!gender.isEmpty()) {
                queryWrapper.like("gender", gender);
            }
        }

        // 执行分页查询
        Page<Patient> resultPage = patientService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(resultPage);
    }

}