package com.example.ending.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ending.common.Constants;
import com.example.ending.common.Result;
import com.example.ending.controller.dto.DoctorLocationDTO;
import com.example.ending.entity.Department;
import com.example.ending.entity.Doctor;
import com.example.ending.entity.DoctorLocation;
import com.example.ending.service.IDoctorLocationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/doctorlocations")
public class DoctorLocationController {


    @Resource
    private IDoctorLocationService doctorLocationService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public Result addDoctorLocation(@RequestBody DoctorLocationDTO doctorLocationDTO) {
        try {
            // 检查医生和部门是否存在并且是否匹配
            if (!validateDoctorDepartment(doctorLocationDTO.getDoctorName(), doctorLocationDTO.getDepartmentName())) {
                return Result.error(Constants.CODE_400, "The doctor's specialty does not match the department or the doctor/department does not exist");
                //医生专业与部门不匹配或医生/部门不存在
            }
            // 转换DTO为实体类并添加医生位置
            DoctorLocation doctorLocation = convertToDoctorLocation(doctorLocationDTO);
            boolean isSuccess = doctorLocationService.addDoctorLocation(doctorLocation);
            return isSuccess ? Result.success() : Result.error(Constants.CODE_500, "Failed to add doctor location");
            //添加医生位置失败
        } catch (RuntimeException e) {
            return Result.error(Constants.CODE_500, e.getMessage());
        }
    }

    // 验证医生是否属于指定部门的私有方法
    private boolean validateDoctorDepartment(String doctorName, String departmentName) {
        // 查询医生信息
        Doctor doctor = doctorLocationService.findDoctorByName(doctorName);
        if (doctor == null) {
            throw new RuntimeException("Doctor information cannot be identified");
            //医生信息无法识别
        }

        // 查询部门信息
        Department department = doctorLocationService.findDepartmentByName(departmentName);
        if (department == null) {
            throw new RuntimeException("Department information cannot be identified");
            //部门信息无法识别
        }
        // 比较医生的专业描述（specialization）和部门名称（name）
        return doctor.getSpecialization().equals(department.getName());
    }

    private DoctorLocation convertToDoctorLocation(DoctorLocationDTO dto) {
        DoctorLocation doctorLocation = new DoctorLocation();

        Doctor doctor = doctorLocationService.findDoctorByName(dto.getDoctorName());
        Department department = doctorLocationService.findDepartmentByName(dto.getDepartmentName());

        if (doctor == null || department == null) {
            throw new RuntimeException("Doctor or department does not exist");
            //医生或部门不存在
        }
        if (!validateDoctorDepartment(dto.getDoctorName(), dto.getDepartmentName())) {
            throw new RuntimeException("Doctor’s specialty and department do not match");
            //医生专业与部门不匹配
        }
        doctorLocation.setDoctorId(doctor.getDoctorId());
        doctorLocation.setDepartmentId(department.getDepartmentId());
        doctorLocation.setRoom(dto.getRoom());
        doctorLocation.setTimeTable(dto.getTimeTable());
        doctorLocation.setDoctorLevel(dto.getDoctorLevel());

        return doctorLocation;
    }

    //编辑更新接口
    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public Result saveOrUpdateDoctorLocation(@RequestBody DoctorLocationDTO doctorLocationDTO) {
        try {
            boolean isSuccess = doctorLocationService.saveOrUpdateDoctorLocation(doctorLocationDTO);
            return isSuccess ? Result.success() : Result.error(Constants.CODE_500, "operation failed");
            //操作失败
        } catch (RuntimeException e) {
            return Result.error(Constants.CODE_500, e.getMessage());
        }
    }
    //编辑更新接口

    @GetMapping("/details")
    public Result findAllDoctorLocationDetails() {
        return Result.success(doctorLocationService.findAllDoctorLocationDetails());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    // 删除医生位置信息
    @DeleteMapping("/{doctorId}/{DepartmentID}")
    public Result deleteDoctorLocation(@PathVariable Integer doctorId, @PathVariable Integer DepartmentID) {
        boolean success = doctorLocationService.deleteByDoctorIdAndDepartmentID(doctorId, DepartmentID);
        return Result.success(success);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    // 批量删除医生位置信息
    @PostMapping("/del/batch")
    public Result deleteDoctorLocationBatch(@RequestBody List<DoctorLocationID> ids) {
        return Result.success(doctorLocationService.removeByIds(ids));
    }

    // 查找所有医生位置信息
    @GetMapping
    public Result findAllDoctorLocations() {
        return Result.success(doctorLocationService.list());
    }

    // 分页查询医生位置信息
    @GetMapping("/page")
    public Result findDoctorLocationPage(@RequestParam Integer pageNum,
                                         @RequestParam Integer pageSize,
                                         @RequestParam(defaultValue = "") String doctorId,
                                         @RequestParam(defaultValue = "") String departmentId) {
        QueryWrapper<DoctorLocation> queryWrapper = new QueryWrapper<>();
        if (!"".equals(doctorId)) {
            queryWrapper.eq("doctorId", doctorId);
        }
        if (!"".equals(departmentId)) {
            queryWrapper.eq("departmentId", departmentId);
        }
        return Result.success(doctorLocationService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    static class DoctorLocationID implements Serializable {
        private Integer doctorId;
        private Integer departmentId;

        // 无参构造函数
        public DoctorLocationID() {
        }

        public DoctorLocationID(Integer doctorID, Integer departmentID) {
            this.doctorId = doctorID;
            this.departmentId = departmentID;
        }

        public Integer getDoctorID() {
            return doctorId;
        }

        public Integer getDepartmentID() {
            return departmentId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DoctorLocationID that = (DoctorLocationID) o;
            return Objects.equals(doctorId, that.doctorId) &&
                    Objects.equals(departmentId, that.departmentId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(doctorId, departmentId);
        }
    }
}
