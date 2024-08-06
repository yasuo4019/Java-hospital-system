package com.example.ending.mapper;

import com.example.ending.entity.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


public interface DepartmentMapper extends BaseMapper<Department> {

    //为DoctorLocation服务
    @Select("SELECT * FROM departments WHERE name = #{name}")
    Department findByName(@Param("name") String departmentName);

}
