package com.heng.property_manager.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heng.property_manager.pojo.entity.Employee;
import com.heng.property_manager.pojo.entity.Equipment;
import com.heng.property_manager.pojo.entity.Maintenance;
import com.heng.property_manager.pojo.entity.Purchase;
import com.heng.property_manager.pojo.response.ResponseCode;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.pojo.vo.MaintenanceVo;
import com.heng.property_manager.service.EmployeeService;
import com.heng.property_manager.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("equipment")
public class EquipmentController {
    private static final int PAGE_SIZE = 8;

    @Autowired
    private EquipmentService equipmentService;

    @PostMapping("searchEquipments")
    public ResponseResult searchEquipments(@RequestBody Equipment equipment){
        Page<Equipment> page = PageHelper.startPage((int)equipment.getParams().get("currentPage"), PAGE_SIZE);
        equipmentService.searchEquipments(equipment);
        PageInfo<Equipment> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("searchMaintenances")
    public ResponseResult searchMaintenances(@RequestBody MaintenanceVo maintenanceVo){
        Page<MaintenanceVo> page = PageHelper.startPage((int)maintenanceVo.getParams().get("currentPage"), PAGE_SIZE);
        equipmentService.searchMaintenances(maintenanceVo);
        PageInfo<MaintenanceVo> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("addEquipment")
    public ResponseResult addEquipment(@RequestBody Equipment equipment){
        boolean is_succeed = equipmentService.addEquipment(equipment);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"添加成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败",null);
        }
    }

    @PostMapping("editEquipment")
    public ResponseResult editEquipment(@RequestBody Equipment equipment){
        boolean is_succeed =equipmentService.editEquipment(equipment);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"修改成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败",null);
        }
    }

    @PostMapping("deleteEquipment")
    public ResponseResult deleteEquipment(@RequestBody Equipment equipment){
        boolean is_succeed =equipmentService.deleteEquipment(equipment);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"删除失败",null);
        }
    }

    @PostMapping("addMaintenance")
    public ResponseResult addMaintenance(@RequestBody Maintenance maintenance){
        boolean is_succeed = equipmentService.addMaintenance(maintenance);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"添加成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败",null);
        }
    }

    @PostMapping("editMaintenance")
    public ResponseResult editMaintenance(@RequestBody Maintenance maintenance){
        boolean is_succeed =equipmentService.editMaintenance(maintenance);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"修改成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败",null);
        }
    }

    @PostMapping("deleteMaintenance")
    public ResponseResult deleteMaintenance(@RequestBody Maintenance maintenance){
        boolean is_succeed =equipmentService.deleteMaintenance(maintenance);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"删除失败",null);
        }
    }
}
