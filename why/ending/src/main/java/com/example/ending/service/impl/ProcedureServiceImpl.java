package com.example.ending.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ending.entity.Procedure;
import com.example.ending.mapper.ProcedureMapper;
import com.example.ending.service.IProcedureService;
import org.springframework.stereotype.Service;


@Service
public class ProcedureServiceImpl extends ServiceImpl<ProcedureMapper, Procedure> implements IProcedureService {

}
