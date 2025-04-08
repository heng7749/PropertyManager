package com.heng.property_manager.controller;

import com.heng.property_manager.pojo.entity.BillType;
import com.heng.property_manager.pojo.entity.Building;
import com.heng.property_manager.pojo.entity.Department;
import com.heng.property_manager.pojo.entity.ParkingLot;
import com.heng.property_manager.pojo.response.ResponseCode;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("common")
public class CommonController {
    @Autowired
    private CommonService commonService;

    @GetMapping("departments")
    public ResponseResult getDepartments(){
        List<Department> departments = commonService.listDepartments();
        return new ResponseResult(ResponseCode.SUCCESS.getCode(),"成功获取",departments);
    }

    @GetMapping("buildings")
    public ResponseResult getBuildings(){
        List<Building> buildings = commonService.listBuildings();
        return new ResponseResult(ResponseCode.SUCCESS.getCode(),"成功获取",buildings);
    }

    @GetMapping("parkingLots")
    public ResponseResult getParkingLots(){
        List<ParkingLot> parkingLots = commonService.listParkingLots();
        return new ResponseResult(ResponseCode.SUCCESS.getCode(),"成功获取",parkingLots);
    }

    @GetMapping("billTypes")
    public ResponseResult getBillTypes(){
        List<BillType> billTypes = commonService.listBillTypes();
        return new ResponseResult(ResponseCode.SUCCESS.getCode(),"成功获取",billTypes);
    }
}
