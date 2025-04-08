package com.heng.property_manager.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heng.property_manager.pojo.entity.Duty;
import com.heng.property_manager.pojo.entity.Employee;
import com.heng.property_manager.pojo.entity.House;
import com.heng.property_manager.pojo.response.ResponseCode;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.pojo.vo.DutyVo;
import com.heng.property_manager.service.DutyService;
import com.heng.property_manager.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("duty")
public class DutyController {
    private static final int PAGE_SIZE = 8;

    @Autowired
    private DutyService dutyService;

    @PostMapping("searchDuties")
    public ResponseResult searchDuties(@RequestBody DutyVo dutyVo){
        List<DutyVo> list =dutyService.searchDuty(dutyVo);
        Map<String, List<DutyVo>> result = list.stream().collect(Collectors.groupingBy(DutyVo::getAttendedDate));
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",result);
    }

    @PostMapping("addDuty")
    public ResponseResult addDuty(@RequestBody Duty duty){
        boolean is_succeed =dutyService.addDuty(duty);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"添加成功·",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败·",null);
        }
    }

    @PostMapping("deleteDuty")
    public ResponseResult deleteDuty(@RequestBody Duty duty){
        boolean is_succeed =dutyService.deleteDuty(duty.getId());
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"删除失败",null);
        }
    }

    @PostMapping("searchDutyByAttendedDate")
    public ResponseResult searchDutyByAttendedDate(@RequestBody DutyVo dutyVo){
        List<DutyVo> list = dutyService.searchDutyByAttendedDate(dutyVo);
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",list);
    }
}
