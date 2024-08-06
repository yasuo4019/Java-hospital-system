package com.example.ending.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ending.common.Result;
import com.example.ending.entity.Department;
import com.example.ending.service.IDepartmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

/**
 * <p>
 * 前端控制器 for Department
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Resource
    private IDepartmentService departmentService;

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    // 新增或者更新部门信息
    @PostMapping
    public Result saveOrUpdateDepartment(@RequestBody Department department) {
        return Result.success(departmentService.saveOrUpdate(department));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    // 删除部门信息
    @DeleteMapping("/{departmentId}")
    public Result deleteDepartment(@PathVariable Integer departmentId) {
        return Result.success(departmentService.removeById(departmentId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    // 批量删除部门信息
    @PostMapping("/del/batch")
    public Result deleteDepartmentBatch(@RequestBody List<Integer> departmentIds) {
        return Result.success(departmentService.removeByIds(departmentIds));
    }


    // 查找所有部门信息
    @GetMapping
    public Result findAllDepartments() {
        return Result.success(departmentService.list());
    }

    // 根据部门ID查找部门
    @GetMapping("/{DepartmentID}")
    public Result findDepartmentById(@PathVariable Integer DepartmentID) {
        return Result.success(departmentService.getById(DepartmentID));
    }

    // 分页查询部门信息
    @GetMapping("/page")
    public Result findDepartmentPage(@RequestParam Integer pageNum,
                                     @RequestParam Integer pageSize,
                                     @RequestParam(defaultValue = "") String name) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("departmentId");
        if (!"".equals(name)) {
            queryWrapper.like("Name", name);
        }
        return Result.success(departmentService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}
