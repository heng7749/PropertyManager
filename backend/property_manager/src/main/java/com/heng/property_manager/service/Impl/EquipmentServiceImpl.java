package com.heng.property_manager.service.Impl;

import com.heng.property_manager.mapper.EquipmentMapper;
import com.heng.property_manager.pojo.entity.Equipment;
import com.heng.property_manager.pojo.entity.LoginUser;
import com.heng.property_manager.pojo.entity.Maintenance;
import com.heng.property_manager.pojo.vo.EquipmentVo;
import com.heng.property_manager.pojo.vo.MaintenanceVo;
import com.heng.property_manager.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Override
    public List<EquipmentVo> searchEquipments(Equipment equipment) {
        return equipmentMapper.searchEquipments(equipment);
    }

    @Override
    public List<MaintenanceVo> searchMaintenances(MaintenanceVo maintenanceVo) {
        List<MaintenanceVo> maintenanceVos = equipmentMapper.searchMaintenances(maintenanceVo);
        for (MaintenanceVo vo : maintenanceVos) {
            int replacePeriod = vo.getReplacePeriod();
            Date deployTime = vo.getDeployTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(deployTime);
            calendar.add(Calendar.DATE,7 * replacePeriod);
            vo.setReplaceTargetDate(calendar.getTime());
        }
        return maintenanceVos;
    }

    @Override
    public boolean addEquipment(Equipment equipment) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 获取员工id
        equipment.setCreateBy(principal.getUser().getBindId());
        equipment.setCreateTime(new Date());
        return equipmentMapper.addEquipment(equipment);
    }

    @Override
    public boolean editEquipment(Equipment equipment) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 获取员工id
        equipment.setUpdateBy(principal.getUser().getBindId());
        equipment.setUpdateTime(new Date());
        return equipmentMapper.editEquipment(equipment);
    }

    @Override
    public boolean deleteEquipment(Equipment equipment) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 获取员工id
        equipment.setUpdateBy(principal.getUser().getBindId());
        equipment.setUpdateTime(new Date());
        return equipmentMapper.deleteEquipment(equipment);
    }

    @Override
    public boolean addMaintenance(Maintenance maintenance) {
        Date last_maintain_date = equipmentMapper.getLastMaintainDateByEquipmentId(maintenance.getId());
        Equipment equipment = equipmentMapper.getEquipmentById(maintenance.getEquipmentId());
        if (equipment == null)
            return false;
        //计算定检日期
        Calendar calendar = Calendar.getInstance();
        if (last_maintain_date != null) {
            calendar.setTime(last_maintain_date);
        } else {
            calendar.setTime(equipment.getDeployTime());
        }
        calendar.add(Calendar.DATE,7 * equipment.getRepairsPeriod());
        Date maintainTargetDate = calendar.getTime();
        maintenance.setMaintainTargetDate(new java.sql.Date(maintainTargetDate.getTime()));
        return equipmentMapper.addMaintenance(maintenance);
    }

    @Override
    public boolean autoAddMaintenance(Maintenance maintenance) {
        return equipmentMapper.autoAddMaintenance(maintenance);
    }

    @Override
    public boolean editMaintenance(Maintenance maintenance) {
        return equipmentMapper.editMaintenance(maintenance);
    }

    @Override
    public boolean deleteMaintenance(Maintenance maintenance) {
        return equipmentMapper.deleteMaintenance(maintenance);
    }

    @Override
    public List<Equipment> findUnMaintainEquipment() {
        return equipmentMapper.findUnMaintainEquipment();
    }

    @Override
    public Maintenance getEquipmentLastMaintainMsg(Long id) {
        return equipmentMapper.getEquipmentLastMaintainMsg(id);
    }

    @Override
    public List<Equipment> listEquipments() {
        return equipmentMapper.listEquipments();
    }

    @Override
    public boolean updateStatus(Maintenance maintenance) {
        return equipmentMapper.updateStatus(maintenance);
    }
}
