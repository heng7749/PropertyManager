package com.heng.property_manager.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heng.property_manager.pojo.entity.Bill;
import com.heng.property_manager.pojo.entity.Complaint;
import com.heng.property_manager.pojo.entity.Owner;
import com.heng.property_manager.pojo.response.ResponseCode;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.pojo.vo.BillVo;
import com.heng.property_manager.pojo.vo.ComplaintVo;
import com.heng.property_manager.pojo.vo.OwnerVo;
import com.heng.property_manager.service.BillService;
import com.heng.property_manager.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bill")
public class BillController {
    private static final int PAGE_SIZE = 8;

    @Autowired
    private BillService billService;

    @PostMapping("searchBill")
    public ResponseResult searchBill(@RequestBody BillVo billVo){
        Page<BillVo> page = PageHelper.startPage((int)billVo.getParams().get("currentPage"), PAGE_SIZE);
        billService.searchBill(billVo);
        PageInfo<BillVo> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("addBill")
    public ResponseResult addBill(@RequestBody Bill bill){
        boolean is_succeed =billService.addBill(bill);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"添加成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败",null);
        }
    }

    @PostMapping("editBill")
    public ResponseResult editBill(@RequestBody Bill bill){
        boolean is_succeed =billService.editBill(bill);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"修改成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败",null);
        }
    }

    @PostMapping("deleteBill")
    public ResponseResult deleteBill(@RequestBody Bill bill){
        boolean is_succeed =billService.deleteBill(bill);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"删除失败",null);
        }
    }

    @PostMapping("myBills")
    public ResponseResult myBills(@RequestBody BillVo billVo){
        Page<BillVo> page = PageHelper.startPage((int)billVo.getParams().get("currentPage"), PAGE_SIZE);
        billService.myBills(billVo);
        PageInfo<BillVo> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }
}
