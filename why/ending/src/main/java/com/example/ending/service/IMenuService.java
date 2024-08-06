package com.example.ending.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ending.entity.Menu;

import java.util.List;

public interface IMenuService extends IService<Menu> {
    List<Menu> findMenus(String name);
}
