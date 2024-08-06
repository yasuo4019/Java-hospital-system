package com.example.ending.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ending.entity.Procedure;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface ProcedureMapper extends BaseMapper<Procedure> {

    //为treatmenthistory表服务
    @Select("SELECT procedureId FROM procedures WHERE name = #{name} AND type = #{type} AND description = #{description}")
    Integer findProcedureIdByNameTypeAndDescription(@Param("name") String name, @Param("type") String type, @Param("description") String description);
}
