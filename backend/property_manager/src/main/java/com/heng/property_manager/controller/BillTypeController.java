package com.heng.property_manager.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heng.property_manager.pojo.entity.BillType;
import com.heng.property_manager.pojo.response.ResponseCode;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.pojo.vo.BillTypeVo;
import com.heng.property_manager.pojo.vo.DepartmentVo;
import com.heng.property_manager.service.BillTypeService;
import com.heng.property_manager.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("billType")
public class BillTypeController {
    private static final int PAGE_SIZE = 8;

    @Autowired
    private BillTypeService billTypeService;

    @PostMapping("searchBillType")
    public ResponseResult searchBillType(@RequestBody BillType billType){
        Page<BillTypeVo> page = PageHelper.startPage((int)billType.getParams().get("currentPage"), PAGE_SIZE);
        billTypeService.searchBillType(billType);
        PageInfo<BillTypeVo> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("addBillType")
    public ResponseResult addBillType(@RequestBody BillType billType){
        boolean is_succeed;
        try {
            is_succeed = billTypeService.addBillType(billType);
        } catch (Exception exception) {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败,收费项已存在",null);
        }
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"添加成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败",null);
        }
    }

    @PostMapping("editBillType")
    public ResponseResult editBillType(@RequestBody BillType billType){
        boolean is_succeed;
        try {
            is_succeed = billTypeService.editBillType(billType);
        } catch (Exception exception) {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败,收费项已存在",null);
        }
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"修改成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败",null);
        }
    }

    @PostMapping("deleteBillType")
    public ResponseResult deleteBillType(@RequestBody BillType billType){
        boolean is_succeed =billTypeService.deleteBillType(billType);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"删除失败",null);
        }
    }

}
