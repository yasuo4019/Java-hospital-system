package com.example.ending.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ending.common.Result;
import com.example.ending.entity.Procedure;
import com.example.ending.service.IProcedureService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
@RestController
@RequestMapping("/procedures")
public class ProcedureController {

    @Resource
    private IProcedureService procedureService;

    // 新增或者更新程序信息
    @PostMapping
    public Result saveOrUpdateProcedure(@RequestBody Procedure procedure) {
        return Result.success(procedureService.saveOrUpdate(procedure));
    }

    // 删除程序信息
    @DeleteMapping("/{procedureId}")
    public Result deleteProcedure(@PathVariable Integer procedureId) {
        return Result.success(procedureService.removeById(procedureId));
    }

    // 批量删除程序信息
    @PostMapping("/del/batch")
    public Result deleteProcedureBatch(@RequestBody List<Integer> procedureIds) {
        return Result.success(procedureService.removeByIds(procedureIds));
    }

    // 查找所有程序信息
    @GetMapping
    public Result findAllProcedures() {
        return Result.success(procedureService.list());
    }

    // 根据程序ID查找程序
    @GetMapping("/{procedureId}")
    public Result findProcedureById(@PathVariable Integer procedureId) {
        return Result.success(procedureService.getById(procedureId));
    }

    // 分页查询程序信息
    @GetMapping("/page")
    public Result findProcedurePage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(defaultValue = "") String name,
                                    @RequestParam(defaultValue = "") String type) {
        QueryWrapper<Procedure> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("procedureId");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        if (!"".equals(type)) {
            queryWrapper.like("type", type);
        }
        return Result.success(procedureService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}
