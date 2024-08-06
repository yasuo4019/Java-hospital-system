package com.example.ending.service;

import com.example.ending.entity.Patient;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * <p>
 * 服务类 for Patient
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
public interface IPatientService extends IService<Patient> {
    Patient getPatientByUsername(String username);
}
