package com.heng.property_manager.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heng.property_manager.mapper.UserMapper;
import com.heng.property_manager.pojo.entity.*;
import com.heng.property_manager.pojo.response.ResponseCode;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.pojo.vo.AdviseVo;
import com.heng.property_manager.pojo.vo.NoticeVo;
import com.heng.property_manager.service.HouseService;
import com.heng.property_manager.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("notice")
public class NoticeController {
    private static final int PAGE_SIZE = 8;

    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserMapper userMapper;

    @PostMapping("publish")
    public ResponseResult noticePublish(@RequestBody Notice notice){
        boolean is_succeed = noticeService.noticePublish(notice);
        String message = null;
        int status_code;
        if (is_succeed) {
            status_code = ResponseCode.SUCCESS.getCode();
            message = "发布成功";
        } else {
            status_code = ResponseCode.ERROR.getCode();
            message = "发布失败";
        }
        return new ResponseResult<>(status_code,message,null);
    }

    @PostMapping("searchNotices")
    public ResponseResult searchNotices(@RequestBody Notice notice){
        Page<NoticeVo> page = PageHelper.startPage((int)notice.getParams().get("currentPage"), PAGE_SIZE);
        noticeService.searchNotices(notice);
        PageInfo<NoticeVo> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("searchUserNotices")
    public ResponseResult searchUserNotices(@RequestBody NoticeVo noticeVo){
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        noticeVo.setUserType(principal.getUser().getType());
        if (noticeVo.getUserType() == 1) {
            noticeVo.setDepartmentId(userMapper.searchDepartmentByEmployeeId(principal.getUser().getBindId()));
        }
        Page<NoticeVo> page = PageHelper.startPage((int)noticeVo.getParams().get("currentPage"), PAGE_SIZE);
        noticeService.searchUserNotices(noticeVo);
        PageInfo<NoticeVo> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @GetMapping("noticeDetail")
    public ResponseResult searchNoticeDetail(@RequestParam Long noticeId){
        List<NoticeVo> result = null;
        int status_code;
        String message = null;
        if (noticeId != null) {
            result = noticeService.searchNoticeById(noticeId);
        }
        if (result != null) {
            status_code = ResponseCode.SUCCESS.getCode();
            message = "成功获取";
        } else {
            status_code = ResponseCode.ERROR.getCode();
            message = "获取失败";
        }
        return new ResponseResult<>(status_code,message,result);
    }

    @PostMapping("editNotice")
    public ResponseResult editNotice(@RequestBody Notice notice){
        boolean is_succeed =noticeService.editNotice(notice);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"修改成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败",null);
        }
    }

    @PostMapping("deleteNotice")
    public ResponseResult deleteNotice(@RequestBody Notice notice){
        boolean is_succeed =noticeService.deleteNotice(notice);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"删除失败",null);
        }
    }
}
