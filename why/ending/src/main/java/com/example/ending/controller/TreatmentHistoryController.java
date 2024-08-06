package com.example.ending.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ending.common.Constants;
import com.example.ending.common.Result;
import com.example.ending.controller.dto.TreatmentHistoryDTO;
import com.example.ending.controller.dto.TreatmentHistoryDetailsDTO;
import com.example.ending.entity.TreatmentHistory;
import com.example.ending.service.ITreatmentHistoryService;
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
 * 前端控制器 for TreatmentHistory
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
@RestController
@RequestMapping("/treatmentHistory")
public class TreatmentHistoryController {

    @Resource
    private ITreatmentHistoryService treatmentHistoryService;


    @PostMapping
    public Result saveOrUpdateTreatmentHistory(@RequestBody TreatmentHistoryDTO dto) {
        try {
            boolean success = treatmentHistoryService.saveOrUpdateTreatmentHistory(dto);
            if (success) {
                return Result.success("Successful operation");
                //操作成功
            } else {
                return Result.failure();
            }
        } catch (RuntimeException e) {
            return Result.failure();
        }
    }


    @DeleteMapping("/{id}")
    public Result deleteTreatmentHistory(@PathVariable Integer id) {
        return Result.success(treatmentHistoryService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteTreatmentHistoryBatch(@RequestBody List<Integer> ids) {
        return Result.success(treatmentHistoryService.removeByIds(ids));
    }

    @GetMapping
    public Result findAllTreatmentHistories() {
        return Result.success(treatmentHistoryService.list());
    }

    @GetMapping("/{id}")
    public Result findTreatmentHistoryById(@PathVariable Integer id) {
        return Result.success(treatmentHistoryService.getById(id));
    }

    @GetMapping("/details")
    public Result getTreatmentHistoryDetails(Authentication authentication) {
        // 获取当前用户的角色
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isUserRole = authorities.contains(new SimpleGrantedAuthority("ROLE_USER"));

        // 如果用户角色是ROLE_USER
        if (isUserRole) {
            Object principal = authentication.getPrincipal();
            String currentUserName = null;

            if (principal instanceof com.example.ending.entity.User) {
                com.example.ending.entity.User customUser = (com.example.ending.entity.User) principal;
                currentUserName = customUser.getUsername(); // 直接从自定义 User 对象获取用户名
            }

            if (currentUserName != null && !currentUserName.isEmpty()) {
                List<TreatmentHistoryDetailsDTO> userTreatmentHistory = treatmentHistoryService.getTreatmentHistoryForUser(currentUserName);
                return Result.success(userTreatmentHistory);
            } else {
                return Result.error(Constants.CODE_500, "username was not passed");
                //username没被传
            }
        } else {
            // 对于其他角色，返回所有治疗历史详情
            List<TreatmentHistoryDetailsDTO> allTreatmentHistory = treatmentHistoryService.findTreatmentHistoryDetails();
            return Result.success(allTreatmentHistory);
        }
    }




    @GetMapping("/page")
    public Result findTreatmentHistoryPage(@RequestParam Integer pageNum,
                                           @RequestParam Integer pageSize) {
        QueryWrapper<TreatmentHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("treatmentHistoryId");
        return Result.success(treatmentHistoryService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }


}
