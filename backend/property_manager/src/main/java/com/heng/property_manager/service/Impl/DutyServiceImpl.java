package com.heng.property_manager.service.Impl;

import com.heng.property_manager.mapper.DutyMapper;
import com.heng.property_manager.pojo.entity.Duty;
import com.heng.property_manager.pojo.entity.LoginUser;
import com.heng.property_manager.pojo.vo.DutyVo;
import com.heng.property_manager.service.DutyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DutyServiceImpl implements DutyService {
    @Autowired
    private DutyMapper dutyMapper;

    @Override
    public List<DutyVo> searchDuty(DutyVo dutyVo) {
        return dutyMapper.searchDuty(dutyVo);
    }

    @Override
    public boolean addDuty(Duty duty) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        duty.setCreateBy(principal.getUser().getId());
        return dutyMapper.addDuty(duty);
    }

    @Override
    public boolean deleteDuty(Long id) {
        return dutyMapper.deleteDuty(id);
    }

    @Override
    public List<DutyVo> searchDutyByAttendedDate(DutyVo dutyVo) {
        return dutyMapper.searchDutyByAttendedDate(dutyVo);
    }
}
