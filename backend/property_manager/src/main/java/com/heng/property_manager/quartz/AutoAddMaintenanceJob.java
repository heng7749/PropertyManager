package com.heng.property_manager.quartz;

import com.heng.property_manager.pojo.entity.Equipment;
import com.heng.property_manager.pojo.entity.Maintenance;
import com.heng.property_manager.service.EquipmentService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.List;

public class AutoAddMaintenanceJob implements Job {
    @Autowired
    private EquipmentService equipmentService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Calendar calendar = null;
        List<Equipment> equipmentList = equipmentService.listEquipments();
        for (Equipment equipment : equipmentList) {
            calendar = Calendar.getInstance();
            Maintenance maintenanceMsg = equipmentService.getEquipmentLastMaintainMsg(equipment.getId());
            // 已有检修记录
            if (maintenanceMsg != null) {
                if (calendar.after(maintenanceMsg.getMaintainTargetDate())) {
                    if (maintenanceMsg.getMaintainDate() != null) {
                        // 添加新的检修计划
                        calendar.setTime(maintenanceMsg.getMaintainDate());
                        calendar.add(Calendar.DATE,7 * equipment.getRepairsPeriod());
                        maintenanceMsg.setMaintainTargetDate(new java.sql.Date(calendar.getTime().getTime()));
                        equipmentService.autoAddMaintenance(maintenanceMsg);
                    } else {
                        // 更新状态为已逾期
                        maintenanceMsg.setStatus(3);
                        equipmentService.updateStatus(maintenanceMsg);
                    }
                }
            } else {
                // 添加设备检修计划
                maintenanceMsg = new Maintenance();
                calendar.setTime(equipment.getDeployTime());
                calendar.add(Calendar.DATE,7 * equipment.getRepairsPeriod());
                maintenanceMsg.setEquipmentId(equipment.getId());
                maintenanceMsg.setMaintainTargetDate(new java.sql.Date(calendar.getTime().getTime()));
                equipmentService.autoAddMaintenance(maintenanceMsg);
            }
        }
    }
}
