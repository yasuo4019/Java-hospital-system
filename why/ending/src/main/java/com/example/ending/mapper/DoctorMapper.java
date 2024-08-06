package com.example.ending.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ending.entity.Doctor;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


public interface DoctorMapper extends BaseMapper<Doctor> {


    @Select("SELECT d.doctorId FROM doctors d " +
            "JOIN doctorlocation dl ON d.doctorId = dl.doctorId " +
            "JOIN departments dp ON dl.departmentId = dp.departmentId " +
            "WHERE d.name = #{doctorName} AND dp.name = #{departmentName}")
    Integer findByNameAndDepartment(@Param("doctorName") String doctorName, @Param("departmentName") String departmentName);

    // //为treatmenthistory表服务
    @Select("SELECT doctorId FROM doctors WHERE name = #{name}")
    Integer findDoctorIdByName(String name);

    //为DoctorLocation服务
    @Select("SELECT * FROM doctors WHERE name = #{name}")
    Doctor findByName(@Param("name") String doctorName);

}
