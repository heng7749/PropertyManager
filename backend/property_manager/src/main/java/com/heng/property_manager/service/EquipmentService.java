package com.heng.property_manager.service;

import com.heng.property_manager.pojo.entity.Equipment;
import com.heng.property_manager.pojo.entity.Maintenance;
import com.heng.property_manager.pojo.vo.EquipmentVo;
import com.heng.property_manager.pojo.vo.MaintenanceVo;

import java.util.List;

public interface EquipmentService {
    List<EquipmentVo> searchEquipments(Equipment equipment);

    List<MaintenanceVo> searchMaintenances(MaintenanceVo maintenanceVo);

    boolean addEquipment(Equipment equipment);

    boolean editEquipment(Equipment equipment);

    boolean deleteEquipment(Equipment equipment);

    boolean addMaintenance(Maintenance maintenance);

    boolean autoAddMaintenance(Maintenance maintenance);

    boolean editMaintenance(Maintenance maintenance);

    boolean deleteMaintenance(Maintenance maintenance);

    List<Equipment> findUnMaintainEquipment();

    Maintenance getEquipmentLastMaintainMsg(Long id);

    List<Equipment> listEquipments();

    boolean updateStatus(Maintenance maintenance);
}
