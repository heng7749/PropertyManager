package com.heng.property_manager.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heng.property_manager.pojo.entity.Building;
import com.heng.property_manager.pojo.response.ResponseCode;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.pojo.vo.BuildingVo;
import com.heng.property_manager.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("building")
public class BuildingController {
    private static final int PAGE_SIZE = 8;

    @Autowired
    private BuildingService buildingService;

    @PostMapping("searchBuilding")
    public ResponseResult searchBuilding(@RequestBody Building building){
        Page<BuildingVo> page = PageHelper.startPage((int)building.getParams().get("currentPage"), PAGE_SIZE);
        buildingService.searchBuilding(building);
        PageInfo<BuildingVo> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("addBuilding")
    public ResponseResult addBuilding(@RequestBody Building building){
        boolean is_succeed;
        try {
            is_succeed = buildingService.addBuilding(building);
        } catch (Exception exception) {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败,楼宇已存在",null);
        }
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"添加成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败",null);
        }
    }

    @PostMapping("editBuilding")
    public ResponseResult editBuilding(@RequestBody Building building){
        boolean is_succeed;
        try {
            is_succeed = buildingService.editBuilding(building);
        } catch (Exception exception) {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败,楼宇已存在",null);
        }
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"修改成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败",null);
        }
    }

    @PostMapping("deleteBuilding")
    public ResponseResult deleteBuilding(@RequestBody Building building){
        boolean is_succeed =buildingService.deleteBuilding(building);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"删除失败",null);
        }
    }
}
