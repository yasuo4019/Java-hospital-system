package com.example.ending.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ending.controller.DoctorLocationController;
import com.example.ending.controller.dto.DoctorLocationDetailsDTO;
import com.example.ending.entity.DoctorLocation;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * 医生位置信息映射接口
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
@Mapper
public interface DoctorLocationMapper extends BaseMapper<DoctorLocation> {

    @Delete("DELETE FROM doctorlocation WHERE doctorId = #{doctorId} AND departmentId = #{departmentId}")
    int deleteByDoctorIdAndDepartmentID(@Param("doctorId") Integer doctorId, @Param("departmentId") Integer departmentId);

    @Select("SELECT dl.doctorId, d.name as doctorName, dl.departmentId, dp.name as departmentName, dl.room, dl.timeTable, dl.doctorLevel " +
            "FROM doctorlocation dl " +
            "JOIN doctors d ON dl.doctorId = d.doctorId " +
            "JOIN departments dp ON dl.departmentId = dp.departmentId")
    List<DoctorLocationDetailsDTO> findAllDoctorLocationDetails();

    @Select("SELECT * FROM doctorlocation WHERE doctorId = #{doctorId} AND departmentId = #{departmentId}")
    DoctorLocation findDoctorLocationByDoctorIdAndDepartmentId(@Param("doctorId") Integer doctorId, @Param("departmentId") Integer departmentId);




    @Update("UPDATE doctorlocation SET room = #{room}, timeTable = #{timeTable}, doctorLevel = #{doctorLevel} WHERE doctorId = #{doctorId} AND departmentId = #{departmentId}")
    int updateDoctorLocation(DoctorLocation doctorLocation);



}
