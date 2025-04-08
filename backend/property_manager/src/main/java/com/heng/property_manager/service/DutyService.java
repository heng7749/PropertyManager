package com.heng.property_manager.service;

import com.heng.property_manager.pojo.entity.Duty;
import com.heng.property_manager.pojo.vo.DutyVo;

import java.util.List;

public interface DutyService {
    List<DutyVo> searchDuty(DutyVo dutyVo);

    boolean addDuty(Duty duty);

    boolean deleteDuty(Long id);

    List<DutyVo> searchDutyByAttendedDate(DutyVo dutyVo);
}
