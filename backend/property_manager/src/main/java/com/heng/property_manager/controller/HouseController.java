package com.heng.property_manager.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heng.property_manager.pojo.entity.Employee;
import com.heng.property_manager.pojo.entity.House;
import com.heng.property_manager.pojo.response.ResponseCode;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.pojo.vo.HouseVo;
import com.heng.property_manager.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("house")
public class HouseController {
    private static final int PAGE_SIZE = 8;

    @Autowired
    private HouseService houseService;

    @PostMapping("searchHouse")
    public ResponseResult searchHouse(@RequestBody House house){
        Page<House> page = PageHelper.startPage((int)house.getParams().get("currentPage"), PAGE_SIZE);
        if (house.getSearchValue() != null) {
            houseService.searchHouseByOwnerName(house);
        } else {
            houseService.searchHouse(house);
        }
        PageInfo<House> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("addHouse")
    public ResponseResult addHouse(@RequestBody House house){
        boolean is_succeed =houseService.addHouse(house);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"添加成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败，房屋已存在",null);
        }
    }

    @PostMapping("editHouse")
    public ResponseResult editHouse(@RequestBody House house){
        boolean is_succeed =houseService.editHouse(house);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"修改成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败",null);
        }
    }

    @PostMapping("deleteHouse")
    public ResponseResult deleteHouse(@RequestBody House house){
        boolean is_succeed =houseService.deleteHouse(house.getId());
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"删除失败",null);
        }
    }

    @GetMapping("ownerHouses")
    public ResponseResult getOwnerHouses(){
        List<HouseVo> ownerHouses = houseService.getOwnerHouses();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",ownerHouses);
    }
}
