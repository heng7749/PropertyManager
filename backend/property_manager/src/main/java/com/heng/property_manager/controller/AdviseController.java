package com.heng.property_manager.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heng.property_manager.pojo.entity.Advise;
import com.heng.property_manager.pojo.entity.Complaint;
import com.heng.property_manager.pojo.response.ResponseCode;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.pojo.vo.AdviseVo;
import com.heng.property_manager.pojo.vo.ComplaintVo;
import com.heng.property_manager.service.AdviseService;
import com.heng.property_manager.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("advise")
public class AdviseController {
    private static final int PAGE_SIZE = 8;

    @Autowired
    private AdviseService adviseService;

    @PostMapping("searchAdvice")
    public ResponseResult searchAdvice(@RequestBody Advise advise){
        Page<AdviseVo> page = PageHelper.startPage((int)advise.getParams().get("currentPage"), PAGE_SIZE);
        adviseService.searchAdvice(advise);
        PageInfo<AdviseVo> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("myAdvice")
    public ResponseResult myAdvice(@RequestBody Advise advise){
        Page<AdviseVo> page = PageHelper.startPage((int)advise.getParams().get("currentPage"), PAGE_SIZE);
        adviseService.myAdvice(advise);
        PageInfo<AdviseVo> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("addAdvice")
    public ResponseResult addAdvice(@RequestBody Advise advise){
        boolean is_succeed = adviseService.addAdvice(advise);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"添加成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败",null);
        }
    }

    @PostMapping("editAdvice")
    public ResponseResult editAdvice(@RequestBody Advise advise){
        boolean is_succeed =adviseService.editAdvice(advise);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"修改成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败",null);
        }
    }

    @PostMapping("conductAdvice")
    public ResponseResult conductAdvice(@RequestBody Advise advise){
        boolean is_succeed =adviseService.conductAdvice(advise);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"处理成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"处理失败",null);
        }
    }

    @PostMapping("deleteAdvice")
    public ResponseResult deleteAdvice(@RequestBody Advise advise){
        boolean is_succeed =adviseService.deleteAdvice(advise);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"删除失败",null);
        }
    }
}
