package com.example.ending.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ending.common.Result;
import com.example.ending.entity.DrugsStore;
import com.example.ending.service.IDrugsStoreService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

/**
 * <p>
 * 前端控制器 for DrugsStore
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
@RestController
@RequestMapping("/drugsstore")
public class DrugsStoreController {

    @Resource
    private IDrugsStoreService drugsStoreService;

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    // 新增或者更新药品信息
    @PostMapping
    public Result saveOrUpdateDrugsStore(@RequestBody DrugsStore drugsStore) {
        return Result.success(drugsStoreService.saveOrUpdate(drugsStore));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    // 删除药品信息
    @DeleteMapping("/{drugId}")
    public Result deleteDrugsStore(@PathVariable Integer drugId) {
        return Result.success(drugsStoreService.removeById(drugId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    // 批量删除药品信息
    @PostMapping("/del/batch")
    public Result deleteDrugsStoreBatch(@RequestBody List<Integer> drugIDs) {
        return Result.success(drugsStoreService.removeByIds(drugIDs));
    }


    // 查找所有药品信息
    @GetMapping
    public Result findAllDrugsStores() {
        return Result.success(drugsStoreService.list());
    }


    // 根据药品ID查找药品
    @GetMapping("/{drugId}")
    public Result findDrugsStoreById(@PathVariable Integer drugId) {
        return Result.success(drugsStoreService.getById(drugId));
    }


    // 分页查询药品信息
    @GetMapping("/page")
    public Result findDrugsStorePage(@RequestParam Integer pageNum,
                                     @RequestParam Integer pageSize,
                                     @RequestParam(defaultValue = "") String name,
                                     @RequestParam(defaultValue = "") String type) {
        QueryWrapper<DrugsStore> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("drugId");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        if (!"".equals(type)) {
            queryWrapper.like("type", type);
        }
        return Result.success(drugsStoreService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}
