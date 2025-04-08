package com.heng.property_manager.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heng.property_manager.pojo.entity.Advise;
import com.heng.property_manager.pojo.entity.Purchase;
import com.heng.property_manager.pojo.entity.Stall;
import com.heng.property_manager.pojo.response.ResponseCode;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.pojo.vo.PurchaseVo;
import com.heng.property_manager.pojo.vo.RepairVo;
import com.heng.property_manager.pojo.vo.StallVo;
import com.heng.property_manager.service.ParkingService;
import com.heng.property_manager.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("purchase")
public class PurchaseController {
    private static final int PAGE_SIZE = 8;

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("searchPurchases")
    public ResponseResult searchPurchases(@RequestBody PurchaseVo purchaseVo){
        Page<PurchaseVo> page = PageHelper.startPage((int)purchaseVo.getParams().get("currentPage"), PAGE_SIZE);
        purchaseService.searchPurchases(purchaseVo);
        PageInfo<PurchaseVo> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("myApplyRecords")
    public ResponseResult myApplyRecords(@RequestBody PurchaseVo purchaseVo){
        Page<PurchaseVo> page = PageHelper.startPage((int)purchaseVo.getParams().get("currentPage"), PAGE_SIZE);
        purchaseService.listUserApplyRecords(purchaseVo);
        PageInfo<PurchaseVo> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("addPurchase")
    public ResponseResult addPurchase(@RequestBody Purchase purchase){
        boolean is_succeed = purchaseService.addPurchase(purchase);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"添加成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败",null);
        }
    }

    @PostMapping("editPurchase")
    public ResponseResult editPurchase(@RequestBody Purchase purchase){
        boolean is_succeed =purchaseService.editPurchase(purchase);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"修改成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败",null);
        }
    }

    @PostMapping("conductPurchase")
    public ResponseResult conductPurchase(@RequestBody Purchase purchase){
        boolean is_succeed =purchaseService.conductPurchase(purchase);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"处理成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"处理失败",null);
        }
    }

    @PostMapping("deletePurchase")
    public ResponseResult deletePurchase(@RequestBody Purchase purchase){
        boolean is_succeed =purchaseService.deletePurchase(purchase);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"删除失败",null);
        }
    }
}
