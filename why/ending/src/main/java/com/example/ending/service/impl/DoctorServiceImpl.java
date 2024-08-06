package com.example.ending.service.impl;

import com.example.ending.entity.Doctor;
import com.example.ending.mapper.DoctorMapper;
import com.example.ending.service.IDoctorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for Doctor
 */
@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements IDoctorService {
}
