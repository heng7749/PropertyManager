package com.heng.property_manager.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heng.property_manager.pojo.entity.Bill;
import com.heng.property_manager.pojo.entity.Complaint;
import com.heng.property_manager.pojo.entity.Notice;
import com.heng.property_manager.pojo.entity.Stall;
import com.heng.property_manager.pojo.response.ResponseCode;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.pojo.vo.ComplaintVo;
import com.heng.property_manager.pojo.vo.PurchaseVo;
import com.heng.property_manager.pojo.vo.StallVo;
import com.heng.property_manager.service.ComplaintService;
import com.heng.property_manager.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("complaint")
public class ComplaintController {
    private static final int PAGE_SIZE = 8;

    @Autowired
    private ComplaintService complaintService;

    @PostMapping("searchComplaints")
    public ResponseResult searchComplaints(@RequestBody Complaint complaint){
        Page<ComplaintVo> page = PageHelper.startPage((int)complaint.getParams().get("currentPage"), PAGE_SIZE);
        complaintService.searchComplaints(complaint);
        PageInfo<ComplaintVo> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("myComplaints")
    public ResponseResult myComplaints(@RequestBody Complaint complaint){
        Page<ComplaintVo> page = PageHelper.startPage((int)complaint.getParams().get("currentPage"), PAGE_SIZE);
        complaintService.myComplaints(complaint);
        PageInfo<ComplaintVo> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("addComplaint")
    public ResponseResult addComplaint(@RequestBody Complaint complaint){
        boolean is_succeed = complaintService.addComplaint(complaint);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"修改成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败",null);
        }
    }

    @PostMapping("editComplaint")
    public ResponseResult editComplaint(@RequestBody Complaint complaint){
        boolean is_succeed =complaintService.editComplaint(complaint);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"修改成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败",null);
        }
    }

    @PostMapping("conductComplaint")
    public ResponseResult conductComplaint(@RequestBody Complaint complaint){
        boolean is_succeed =complaintService.conductComplaint(complaint);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"处理成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"处理失败",null);
        }
    }

    @PostMapping("deleteComplaint")
    public ResponseResult deleteComplaint(@RequestBody Complaint complaint){
        boolean is_succeed =complaintService.deleteComplaint(complaint);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"删除失败",null);
        }
    }
}
