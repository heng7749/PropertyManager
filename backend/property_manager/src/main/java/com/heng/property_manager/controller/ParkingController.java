package com.heng.property_manager.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heng.property_manager.pojo.entity.*;
import com.heng.property_manager.pojo.response.ResponseCode;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.pojo.vo.CarVo;
import com.heng.property_manager.pojo.vo.OwnerVo;
import com.heng.property_manager.pojo.vo.ParkingVo;
import com.heng.property_manager.pojo.vo.StallVo;
import com.heng.property_manager.service.OwnerService;
import com.heng.property_manager.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("parking")
public class ParkingController {
    private static final int PAGE_SIZE = 8;

    @Autowired
    private ParkingService parkingService;

    @PostMapping("searchStall")
    public ResponseResult searchStall(@RequestBody Stall stall){
        Page<StallVo> page = PageHelper.startPage((int)stall.getParams().get("currentPage"), PAGE_SIZE);
        parkingService.searchStall(stall);
        PageInfo<StallVo> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("searchParkingRecords")
    public ResponseResult searchParkingRecords(@RequestBody ParkingVo parkingVo){
        Page<ParkingVo> page = PageHelper.startPage((int)parkingVo.getParams().get("currentPage"), PAGE_SIZE);
        parkingService.searchParkingRecords(parkingVo);
        PageInfo<ParkingVo> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("addStall")
    public ResponseResult addStall(@RequestBody Stall stall){
        boolean is_succeed =parkingService.addStall(stall);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"添加成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败",null);
        }
    }

    @PostMapping("editStall")
    public ResponseResult editStall(@RequestBody StallVo stallVo){
        boolean is_succeed = parkingService.editStall(stallVo);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"修改成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败",null);
        }
    }

    @PostMapping("deleteStall")
    public ResponseResult deleteStall(@RequestBody Stall stall){
        boolean is_succeed =parkingService.deleteStall(stall);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"删除失败",null);
        }
    }

    @PostMapping("stallBindCars")
    public ResponseResult getStallBindCars(@RequestBody Stall stall){
        List<Car> cars = parkingService.getStallBindCars(stall);
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",cars);
    }

    @PostMapping("addParking")
    public ResponseResult addParking(@RequestBody ParkingVo parkingVo){
        boolean is_succeed =parkingService.addParking(parkingVo);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"添加成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败",null);
        }
    }

    @PostMapping("editParking")
    public ResponseResult editParking(@RequestBody Parking parking){
        boolean is_succeed = parkingService.editParking(parking);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"修改成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败",null);
        }
    }

    @PostMapping("deleteParking")
    public ResponseResult deleteParking(@RequestBody Parking parking){
        boolean is_succeed =parkingService.deleteParking(parking);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"删除失败",null);
        }
    }

    @GetMapping("freeStall")
    public ResponseResult getFreeStall(){
        List<Stall> parkingLots = parkingService.getFreeStall();
        return new ResponseResult(ResponseCode.SUCCESS.getCode(),"成功获取",parkingLots);
    }
}
