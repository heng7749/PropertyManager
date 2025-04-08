package com.heng.property_manager.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heng.property_manager.pojo.entity.Department;
import com.heng.property_manager.pojo.response.ResponseCode;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.pojo.vo.BuildingVo;
import com.heng.property_manager.pojo.vo.DepartmentVo;
import com.heng.property_manager.service.BuildingService;
import com.heng.property_manager.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("department")
public class DepartmentController {
    private static final int PAGE_SIZE = 8;

    @Autowired
    private DepartmentService departmentService;

    @PostMapping("searchDepartment")
    public ResponseResult searchDepartment(@RequestBody Department department){
        Page<DepartmentVo> page = PageHelper.startPage((int)department.getParams().get("currentPage"), PAGE_SIZE);
        departmentService.searchDepartment(department);
        PageInfo<DepartmentVo> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("addDepartment")
    public ResponseResult addDepartment(@RequestBody Department department){
        boolean is_succeed;
        try {
            is_succeed = departmentService.addDepartment(department);
        } catch (Exception exception) {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败,部门已存在",null);
        }
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"添加成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败",null);
        }
    }

    @PostMapping("editDepartment")
    public ResponseResult editDepartment(@RequestBody Department department){
        boolean is_succeed;
        try {
            is_succeed = departmentService.editDepartment(department);
        } catch (Exception exception) {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败,部门已存在",null);
        }
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"修改成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败",null);
        }
    }

    @PostMapping("deleteDepartment")
    public ResponseResult deleteDepartment(@RequestBody Department department){
        boolean is_succeed =departmentService.deleteDepartment(department);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"删除失败",null);
        }
    }
}
