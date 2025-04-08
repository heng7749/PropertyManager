package com.heng.property_manager.service.Impl;

import com.heng.property_manager.mapper.RepairMapper;
import com.heng.property_manager.pojo.entity.LoginUser;
import com.heng.property_manager.pojo.entity.Repair;
import com.heng.property_manager.pojo.vo.RepairVo;
import com.heng.property_manager.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RepairServiceImpl implements RepairService {
    @Autowired
    private RepairMapper repairMapper;

    @Override
    public List<RepairVo> searchRepairs(RepairVo repairVo) {
        return repairMapper.searchRepairs(repairVo);
    }

    @Override
    public List<RepairVo> listUserRepairRecords(RepairVo repairVo) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        repairVo.setOwnerId(principal.getUser().getId());
        return repairMapper.listUserRepairRecords(repairVo);
    }

    @Override
    public boolean addRepair(Repair repair) {
        repair.setCreateTime(new Date());
        return repairMapper.addRepair(repair);
    }

    @Override
    public boolean editRepair(Repair repair) {
        return repairMapper.editRepair(repair);
    }

    @Override
    public boolean deleteRepair(Long id) {
        return repairMapper.deleteRepair(id);
    }

    @Override
    public boolean conductRepair(Repair repair) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        repair.setUpdateBy(principal.getUser().getId());
        repair.setUpdateTime(new Date());
        return repairMapper.conductRepair(repair);
    }
}
