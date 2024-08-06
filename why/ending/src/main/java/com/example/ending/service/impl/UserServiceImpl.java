package com.example.ending.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.ending.common.Constants;
import com.example.ending.controller.dto.UserDTO;
import com.example.ending.entity.Menu;
import com.example.ending.entity.User;
import com.example.ending.exception.ServiceException;
import com.example.ending.mapper.RoleMapper;
import com.example.ending.mapper.RoleMenuMapper;
import com.example.ending.mapper.UserMapper;
import com.example.ending.service.IMenuService;
import com.example.ending.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ending.utils.TokenUtils;
import org.springframework.stereotype.Service;
import cn.hutool.crypto.digest.DigestUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log LOG = Log.get();


    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    @Override
    public UserDTO login(UserDTO userDTO) {
        String hashedPassword = DigestUtil.sha256Hex(userDTO.getPassword());
        userDTO.setPassword(hashedPassword);

        User one = getUserInfo(userDTO);
        if (one != null) {
            BeanUtil.copyProperties(one, userDTO, true);

            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDTO.setToken(token);

            String role = one.getRole(); //ROLE_ADMIN
            List<Menu> roleMenus = getRoleMenu(role);
            userDTO.setMenus(roleMenus);

            return userDTO;
        } else {
            throw new ServiceException(Constants.CODE_600, "Wrong User Name Or Password");
        }
    }


    @Override
    public User register(UserDTO userDTO) {
        // 用户密码 SHA256加密
        userDTO.setPassword(DigestUtil.sha256Hex(userDTO.getPassword()));
        User one = getUserInfo(userDTO);
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(userDTO, one, true);
            save(one);  // 把 copy完之后的用户对象存储到数据库
        } else {
            throw new ServiceException(Constants.CODE_600, "User Already Exist");
        }
        return one;
    }

//23
    private User getUserInfo(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        User one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "System Error");
        }
        return one;
    }

    /**
     * 获取当前角色的菜单列表
     * @param roleFlag
     * @return
     */
    private List<Menu> getRoleMenu(String roleFlag) {
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        //当前角色的所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);
        //查出系统所有菜单
        List<Menu> menus = menuService.findMenus("");
        //new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        //筛选当前用户角色菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}