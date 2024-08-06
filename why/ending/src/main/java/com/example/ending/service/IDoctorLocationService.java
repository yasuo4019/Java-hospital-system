package com.example.ending.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ending.controller.dto.DoctorLocationDTO;
import com.example.ending.controller.dto.DoctorLocationDetailsDTO;
import com.example.ending.entity.Department;
import com.example.ending.entity.Doctor;
import com.example.ending.entity.DoctorLocation;

import java.util.List;


/**
 * <p>
 * 医生位置信息服务层接口
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
public interface IDoctorLocationService extends IService<DoctorLocation> {

    boolean deleteByDoctorIdAndDepartmentID(Integer doctorId, Integer departmentId);

    List<DoctorLocationDetailsDTO> findAllDoctorLocationDetails();

    // 根据部门名称查找部门信息
    Department findDepartmentByName(String departmentName);

    // 根据医生名称查找医生信息
    Doctor findDoctorByName(String doctorName);

    // 添加医生位置的方法
    boolean addDoctorLocation(DoctorLocation doctorLocation);


    boolean saveOrUpdateDoctorLocation(DoctorLocationDTO doctorLocationDTO);

    DoctorLocation findDoctorLocationByDoctorIdAndDepartmentId(Integer doctorId, Integer departmentId);

}
