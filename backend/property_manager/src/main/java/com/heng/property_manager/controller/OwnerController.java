package com.heng.property_manager.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heng.property_manager.pojo.entity.House;
import com.heng.property_manager.pojo.entity.Owner;
import com.heng.property_manager.pojo.response.ResponseCode;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.pojo.vo.OwnerVo;
import com.heng.property_manager.service.HouseService;
import com.heng.property_manager.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("owner")
public class OwnerController {
    private static final int PAGE_SIZE = 8;

    @Autowired
    private OwnerService ownerService;

    @PostMapping("searchOwner")
    public ResponseResult searchOwner(@RequestBody OwnerVo ownerVo){
        Page<Owner> page = PageHelper.startPage((int)ownerVo.getParams().get("currentPage"), PAGE_SIZE);
        ownerService.searchByHouse(ownerVo);
        PageInfo<Owner> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("searchOwnerByName")
    public ResponseResult searchOwnerByName(@RequestBody Owner owner){
        Page<Owner> page = PageHelper.startPage((int)owner.getParams().get("currentPage"), PAGE_SIZE);
        ownerService.searchByName(owner);
        PageInfo<Owner> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("addOwner")
    public ResponseResult addOwner(@RequestBody Owner owner){
        boolean is_succeed =ownerService.addOwner(owner);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"添加成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败，业主已存在",null);
        }
    }

    @PostMapping("editOwner")
    public ResponseResult editOwner(@RequestBody Owner owner){
        boolean is_succeed =ownerService.editOwner(owner);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"修改成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败",null);
        }
    }

    @PostMapping("deleteOwner")
    public ResponseResult deleteOwner(@RequestBody Owner owner){
        boolean is_succeed =ownerService.deleteOwner(owner.getId());
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"删除失败",null);
        }
    }
}
