package com.heng.property_manager.service;

import com.heng.property_manager.pojo.entity.Repair;
import com.heng.property_manager.pojo.vo.RepairVo;

import java.util.List;

public interface RepairService {
    List<RepairVo> searchRepairs(RepairVo repairVo);

    List<RepairVo> listUserRepairRecords(RepairVo repairVo);

    boolean addRepair(Repair repair);

    boolean editRepair(Repair repair);

    boolean deleteRepair(Long id);

    boolean conductRepair(Repair repair);
}
