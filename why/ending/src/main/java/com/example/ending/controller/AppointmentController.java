package com.example.ending.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ending.common.Result;
import com.example.ending.controller.dto.AppointmentDTO;
import com.example.ending.controller.dto.AppointmentDetailsDTO;
import com.example.ending.entity.Appointment;
import com.example.ending.service.IAppointmentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 前端控制器 for Appointment
 * </p>
 *
 * @since 2023-05-06
 */
@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    @Resource
    private IAppointmentService appointmentService;

    private Appointment convertDtoToEntity(AppointmentDTO appointmentDto) {
        Integer doctorId = appointmentService.findDoctorIdByName(appointmentDto.getDoctorName());
        Integer patientId = appointmentService.findPatientIdByName(appointmentDto.getPatientName());
        String room = appointmentService.findRoomByDoctorId(doctorId);

        if (doctorId == null || patientId == null) {
            throw new RuntimeException("Doctor or patient information cannot be identified");
            //医生或患者信息无法识别
        }

        if (!room.equals(appointmentDto.getRoom())) {
            throw new RuntimeException("The doctor's room information does not match");
            //医生所在房间信息不匹配
        }

        Appointment appointment = new Appointment();
        appointment.setPatientId(patientId);
        appointment.setDoctorId(doctorId);
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setStartTime(appointmentDto.getStartTime());
        appointment.setEndTime(appointmentDto.getEndTime());

        return appointment;
    }

    @PostMapping
    public Result saveOrUpdateAppointment(@RequestBody AppointmentDTO appointmentDto) {
        Appointment appointment = convertDtoToEntity(appointmentDto);
        boolean success = appointmentService.saveOrUpdate(appointment);

        if (success) {
            return Result.success(appointment);
        } else {
            return Result.failure();
        }
    }

    // 删除预约信息
    @DeleteMapping("/{appointmentId}")
    public Result deleteAppointment(@PathVariable Integer appointmentId) {
        return Result.success(appointmentService.removeById(appointmentId));
    }

    // 批量删除预约信息
    @PostMapping("/del/batch")
    public Result deleteAppointmentBatch(@RequestBody List<Integer> appointmentIds) {
        return Result.success(appointmentService.removeByIds(appointmentIds));
    }

    // 查找所有预约信息
    @GetMapping
    public Result findAllAppointments() {
        return Result.success(appointmentService.list());
    }

    // 根据预约ID查找预约信息
    @GetMapping("/{appointmentId}")
    public Result findAppointmentById(@PathVariable Integer appointmentId) {
        return Result.success(appointmentService.getById(appointmentId));
    }

    @GetMapping("/details")
    public Result findAllAppointmentDetails(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isUserRole = authorities.contains(new SimpleGrantedAuthority("ROLE_USER"));

        if (isUserRole) {
            Object principal = authentication.getPrincipal();
            String currentUserName = null;

            if (principal instanceof com.example.ending.entity.User) {
                com.example.ending.entity.User customUser = (com.example.ending.entity.User) principal;
                currentUserName = customUser.getUsername();
            }

            if (currentUserName != null) {
                List<AppointmentDetailsDTO> userAppointments = appointmentService.getAppointmentsForUser(currentUserName);
                return Result.success(userAppointments);
            } else {
                return Result.error();
            }
        } else {
            List<AppointmentDetailsDTO> allAppointments = appointmentService.findAllAppointmentDetails();
            return Result.success(allAppointments);
        }
    }

    // 分页查询预约信息
    @GetMapping("/page")
    public Result findAppointmentPage(@RequestParam Integer pageNum,
                                      @RequestParam Integer pageSize,
                                      @RequestParam(defaultValue = "") String doctorId,
                                      @RequestParam(defaultValue = "") String patientId) {
        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("appointmentId");
        if (!"".equals(doctorId)) {
            queryWrapper.eq("doctorId", doctorId);
        }
        if (!"".equals(patientId)) {
            queryWrapper.eq("patientId", patientId);
        }
        return Result.success(appointmentService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}
