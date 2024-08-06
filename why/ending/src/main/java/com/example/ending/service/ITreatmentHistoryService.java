package com.example.ending.service;

import com.example.ending.controller.dto.TreatmentHistoryDTO;
import com.example.ending.entity.TreatmentHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ending.controller.dto.TreatmentHistoryDetailsDTO;
import java.util.List;

public interface ITreatmentHistoryService extends IService<TreatmentHistory> {

    List<TreatmentHistoryDetailsDTO> findTreatmentHistoryDetails();
    boolean saveOrUpdateTreatmentHistory(TreatmentHistoryDTO dto);

    //为行级安全服务
    List<TreatmentHistoryDetailsDTO> getTreatmentHistoryForUser(String username);


}
