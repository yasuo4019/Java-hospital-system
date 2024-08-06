package com.example.ending.service.impl;

import com.example.ending.entity.Department;
import com.example.ending.mapper.DepartmentMapper;
import com.example.ending.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for Doctor
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {
}
