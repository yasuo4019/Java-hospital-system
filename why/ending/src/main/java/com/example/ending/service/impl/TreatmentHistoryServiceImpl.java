package com.example.ending.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ending.controller.dto.TreatmentHistoryDTO;
import com.example.ending.entity.TreatmentHistory;
import com.example.ending.mapper.*;
import com.example.ending.service.ITreatmentHistoryService;
import com.example.ending.controller.dto.TreatmentHistoryDetailsDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TreatmentHistoryServiceImpl extends ServiceImpl<TreatmentHistoryMapper, TreatmentHistory> implements ITreatmentHistoryService {

    @Resource
    private TreatmentHistoryMapper treatmentHistoryMapper;
    @Resource
    private DoctorMapper doctorMapper;
    @Resource
    private PatientMapper patientMapper;
    @Resource
    private TreatmentMapper treatmentMapper;
    @Resource
    private DrugsStoreMapper drugsStoreMapper;
    @Resource
    private ProcedureMapper procedureMapper;



    @Override
    public List<TreatmentHistoryDetailsDTO> findTreatmentHistoryDetails() {
        return treatmentHistoryMapper.getDetailedTreatmentHistory();
    }

    public boolean saveOrUpdateTreatmentHistory(TreatmentHistoryDTO dto) {
        Integer doctorId = doctorMapper.findDoctorIdByName(dto.getDoctorName());
        Integer patientId = patientMapper.findPatientIdByName(dto.getPatientName());

        if (doctorId == null || patientId == null) {
            throw new RuntimeException("医生或患者名称无效");
        }
        // 检验医生和患者是否对应同一治疗记录
        Integer treatmentId = treatmentMapper.findTreatmentIdByDoctorIdAndPatientId(doctorId, patientId);
        if (treatmentId == null) {
            throw new RuntimeException("医生和患者不对应同一治疗记录");
        }
        // 正确调用 DrugsStoreMapper 的方法
        Integer drugId = drugsStoreMapper.findDrugIdByNameAndType(dto.getDrugName(), dto.getDrugType());
        if (drugId == null) {
            throw new RuntimeException("药品名称或类型无效");
        }
        // 验证并获取进程ID
        Integer procedureId = procedureMapper.findProcedureIdByNameTypeAndDescription(dto.getProcedureName(), dto.getProcedureType(), dto.getProcedureDescription());
        if (procedureId == null) {
            throw new RuntimeException("进程名称、类型或描述无效");
        }

        TreatmentHistory history = new TreatmentHistory();
        history.setTreatmentId(treatmentId);
        history.setDoctorId(doctorId);
        history.setDrugId(drugId);
        history.setProcedureId(procedureId);
        history.setRecordDate(dto.getRecordDate());
        history.setRecordTime(dto.getRecordTime());
        history.setResult(dto.getResult());

        // 检查是否应该更新或新增
        boolean success;
        if (history.getId() != null && getById(history.getId()) != null) {
            // ID存在，执行更新
            success = updateById(history);
        } else {
            // ID不存在，执行新增
            success = save(history);
        }

        return success;
    }

    //为行级安全服务
    @Override
    public List<TreatmentHistoryDetailsDTO> getTreatmentHistoryForUser(String username) {
        return treatmentHistoryMapper.getTreatmentHistoryForUser(username);
    }

}
