package com.heng.property_manager.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heng.property_manager.pojo.entity.Employee;
import com.heng.property_manager.pojo.response.ResponseCode;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.pojo.vo.DutyVo;
import com.heng.property_manager.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("employee")
public class EmployeeController {
    private static final int PAGE_SIZE = 8;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("current")
    public ResponseResult getCurrentEmployee(@RequestParam("currentPage") int currentPage){
        Page<Employee> page = PageHelper.startPage(currentPage, PAGE_SIZE);
        employeeService.listEmployee();
        PageInfo<Employee> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @GetMapping("searchEmployee")
    public ResponseResult searchEmployee(@RequestParam("currentPage") int currentPage,
                                            @RequestParam("searchType") int searchType,
                                            @RequestParam("searchValue") String searchValue,
                                            @RequestParam(value="departmentId",required=false) Long departmentId){
        PageInfo pageInfo = null;
        if (searchType == 2) {
            long employeeId = 0;
            try {
                employeeId = Long.parseLong(searchValue);
            } catch (Exception e) {
                return new ResponseResult<>(ResponseCode.ERROR.getCode(),"获取失败",null);
            }
            List<Employee> list = employeeService.searchEmployeeById(employeeId);
            pageInfo = new PageInfo();
            pageInfo.setList(list);
            pageInfo.setTotal(list.size());
            pageInfo.setPageSize(1);
            pageInfo.setPageNum(1);
        } else if (searchType == 1) {
            Page<Employee> page = PageHelper.startPage(currentPage, PAGE_SIZE);
            Employee employee = new Employee();
            employee.setName(searchValue);
            employee.setDepartmentId(departmentId);
            employeeService.searchEmployee(employee);
            pageInfo = page.toPageInfo();
        }
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("addEmployee")
    public ResponseResult addEmployee(@RequestBody Employee employee){
        boolean is_succeed =employeeService.addEmployee(employee);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"添加成功·",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败·",null);
        }
    }

    @PostMapping("editEmployee")
    public ResponseResult editEmployee(@RequestBody Employee employee){
        boolean is_succeed =employeeService.editEmployee(employee);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"修改成功·",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败·",null);
        }
    }

    @PostMapping("deleteEmployee")
    public ResponseResult deleteEmployee(@RequestBody Employee employee){
        boolean is_succeed =employeeService.deleteEmployee(employee.getId());
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"删除失败",null);
        }
    }
}
