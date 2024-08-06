package com.example.ending.service;

import com.example.ending.controller.dto.UserDTO;
import com.example.ending.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    User register(UserDTO userDTO);


}
