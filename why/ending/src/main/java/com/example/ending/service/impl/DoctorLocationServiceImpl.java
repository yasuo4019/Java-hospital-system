package com.example.ending.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ending.controller.dto.DoctorLocationDTO;
import com.example.ending.controller.dto.DoctorLocationDetailsDTO;
import com.example.ending.entity.Department;
import com.example.ending.entity.Doctor;
import com.example.ending.entity.DoctorLocation;
import com.example.ending.mapper.DepartmentMapper;
import com.example.ending.mapper.DoctorLocationMapper;
import com.example.ending.mapper.DoctorMapper;
import com.example.ending.service.IDoctorLocationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 医生位置信息服务实现类
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
@Service
public class DoctorLocationServiceImpl extends ServiceImpl<DoctorLocationMapper, DoctorLocation> implements IDoctorLocationService {


    // 注入数据访问对象
    @Resource
    private DoctorMapper doctorMapper;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private DoctorLocationMapper doctorLocationMapper;

    @Override
    public boolean deleteByDoctorIdAndDepartmentID(Integer doctorId, Integer departmentId) {
        return baseMapper.deleteByDoctorIdAndDepartmentID(doctorId, departmentId) > 0;
    }

    @Override
    public List<DoctorLocationDetailsDTO> findAllDoctorLocationDetails() {
        return baseMapper.findAllDoctorLocationDetails();
    }

    // 实现查找部门的方法
    @Override
    public Department findDepartmentByName(String departmentName) {
        return departmentMapper.findByName(departmentName);
    }
    // 实现查找医生的方法
    @Override
    public Doctor findDoctorByName(String doctorName) {
        return doctorMapper.findByName(doctorName);
    }
    public boolean addDoctorLocation(DoctorLocation doctorLocation) {
        return this.save(doctorLocation);
    }


    // 实现从 DTO 到实体的转换方法
    private DoctorLocation convertToDoctorLocation(DoctorLocationDTO dto) {
        if (dto == null) {
            return null;
        }

        DoctorLocation doctorLocation = new DoctorLocation();

        // 根据 doctorName 查询 doctorId
        Doctor doctor = doctorMapper.findByName(dto.getDoctorName());
        if (doctor == null) {
            throw new RuntimeException("找不到医生: " + dto.getDoctorName());
        }

        // 根据 departmentName 查询 departmentId
        Department department = departmentMapper.findByName(dto.getDepartmentName());
        if (department == null) {
            throw new RuntimeException("找不到部门: " + dto.getDepartmentName());
        }

        doctorLocation.setDoctorId(doctor.getDoctorId());
        doctorLocation.setDepartmentId(department.getDepartmentId());
        doctorLocation.setRoom(dto.getRoom());
        doctorLocation.setTimeTable(dto.getTimeTable());
        doctorLocation.setDoctorLevel(dto.getDoctorLevel());

        return doctorLocation;
    }

    @Override
    public boolean saveOrUpdateDoctorLocation(DoctorLocationDTO doctorLocationDTO) {
        // 首先，根据 doctorLocationDTO 转换或获取 doctorLocation 实例
        DoctorLocation doctorLocation = convertToDoctorLocation(doctorLocationDTO);

        // 检查是否存在匹配的记录
        DoctorLocation existingLocation = findDoctorLocationByDoctorIdAndDepartmentId(
                doctorLocation.getDoctorId(), doctorLocation.getDepartmentId());

        if (existingLocation != null) {
            existingLocation.setRoom(doctorLocationDTO.getRoom());
            existingLocation.setTimeTable(doctorLocationDTO.getTimeTable());
            existingLocation.setDoctorLevel(doctorLocationDTO.getDoctorLevel());
            return doctorLocationMapper.updateDoctorLocation(existingLocation) > 0;
        } else {
            // 执行插入操作
            return this.save(doctorLocation);
        }
    }

//    private boolean validateDoctorDepartment(String doctorName, String departmentName) {
//        // 查询医生信息
//        Doctor doctor = doctorMapper.findByName(doctorName);
//        if (doctor == null) {
//            throw new RuntimeException("医生信息无法识别");
//        }
//
//        // 查询部门信息
//        Department department = departmentMapper.findByName(departmentName);
//        if (department == null) {
//            throw new RuntimeException("部门信息无法识别");
//        }
//
//        // 比较医生的专业描述（specialization）和部门名称（name）
//        return doctor.getSpecialization().equals(department.getName());
//    }
//
//    private DoctorLocation convertToDoctorLocation(DoctorLocationDTO dto) {
//        DoctorLocation doctorLocation = new DoctorLocation();
//        Doctor doctor = doctorMapper.findByName(dto.getDoctorName());
//        Department department = departmentMapper.findByName(dto.getDepartmentName());
//
//        if (doctor == null || department == null) {
//            throw new RuntimeException("医生或部门不存在");
//        }
//
//        doctorLocation.setDoctorId(doctor.getDoctorId());
//        doctorLocation.setDepartmentId(department.getDepartmentId());
//        doctorLocation.setRoom(dto.getRoom());
//        doctorLocation.setTimeTable(dto.getTimeTable());
//        doctorLocation.setDoctorLevel(dto.getDoctorLevel());
//
//        return doctorLocation;
//    }
//
//
////    @Override
////    public boolean saveOrUpdateDoctorLocation(DoctorLocationDTO doctorLocationDTO) {
////        // 验证医生是否属于指定部门
////        boolean isValid = validateDoctorDepartment(doctorLocationDTO.getDoctorName(), doctorLocationDTO.getDepartmentName());
////        if (!isValid) {
////            throw new RuntimeException("医生专业与部门不匹配或医生/部门不存在");
////        }
////
////        DoctorLocation doctorLocation = convertToDoctorLocation(doctorLocationDTO);
////
////        // 使用自定义查询方法检查记录是否存在
////        DoctorLocation existingLocation = findDoctorLocationByDoctorIdAndDepartmentId(
////                doctorLocation.getDoctorId(), doctorLocation.getDepartmentId());
////
////        if (existingLocation == null) {
////            // 如果不存在，则插入新记录
////            return this.save(doctorLocation);
////        } else {
////            // 如果存在，则更新记录
////            return this.updateById(doctorLocation);
////        }
////    }


//    @Override
//    public boolean saveOrUpdateDoctorLocation(DoctorLocationDTO doctorLocationDTO) {
//        // 根据名称查找 doctorId 和 departmentId
//        Doctor doctor = doctorMapper.findByName(doctorLocationDTO.getDoctorName());
//        Department department = departmentMapper.findByName(doctorLocationDTO.getDepartmentName());
//
//        if (doctor == null || department == null) {
//            throw new RuntimeException("医生或部门不存在");
//        }
//
//        Integer doctorId = doctor.getDoctorId();
//        Integer departmentId = department.getDepartmentId();
//
//        // 检查是否存在匹配的记录
//        DoctorLocation existingLocation = findDoctorLocationByDoctorIdAndDepartmentId(doctorId, departmentId);
//
//        if (existingLocation != null) {
//            // 执行更新操作，调用自定义的更新方法
//            return doctorLocationMapper.updateDoctorLocation(doctorLocation);
//        } else {
//            // 执行插入操作
//            return this.save(doctorLocation);
//        }
//        if (existingLocation != null) {
//            // 如果存在，更新记录
//            // 设置必要的字段
//            existingLocation.setRoom(doctorLocationDTO.getRoom());
//            existingLocation.setTimeTable(doctorLocationDTO.getTimeTable());
//            existingLocation.setDoctorLevel(doctorLocationDTO.getDoctorLevel());
//            return this.updateById(existingLocation);
//        } else {
//            // 记录不存在，处理逻辑（例如插入新记录或返回错误）
//            return false; // 或者执行插入操作
//        }

    @Override
    public DoctorLocation findDoctorLocationByDoctorIdAndDepartmentId(Integer doctorId, Integer departmentId) {
        return baseMapper.findDoctorLocationByDoctorIdAndDepartmentId(doctorId, departmentId);
    }

}
