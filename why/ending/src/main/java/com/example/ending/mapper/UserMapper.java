package com.example.ending.mapper;

import com.example.ending.controller.dto.UserPasswordDTO;
import com.example.ending.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
public interface UserMapper extends BaseMapper<User> {

    //23
    @Update("update sys_user set password = #{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(UserPasswordDTO userPasswordDTO);

    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    User selectByUsername(String username);
    //23
}
