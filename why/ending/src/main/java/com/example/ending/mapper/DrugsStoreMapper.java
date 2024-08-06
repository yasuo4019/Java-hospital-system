package com.example.ending.mapper;

import com.example.ending.entity.DrugsStore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


public interface DrugsStoreMapper extends BaseMapper<DrugsStore> {

    //为treatmenthistory表服务
    @Select("SELECT drugId FROM drugsstore WHERE name = #{name} AND type = #{type}")
    Integer findDrugIdByNameAndType(@Param("name") String name, @Param("type") String type);

}
