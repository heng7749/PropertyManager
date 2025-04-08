package com.heng.property_manager.service.Impl;

import com.heng.property_manager.mapper.BuildingMapper;
import com.heng.property_manager.mapper.DepartmentMapper;
import com.heng.property_manager.pojo.entity.Department;
import com.heng.property_manager.pojo.entity.LoginUser;
import com.heng.property_manager.pojo.vo.DepartmentVo;
import com.heng.property_manager.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<DepartmentVo> searchDepartment(Department department) {
        return departmentMapper.searchDepartment(department);
    }

    @Override
    public boolean addDepartment(Department department) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        department.setCreateBy(principal.getUser().getBindId());
        department.setCreateTime(new Date());
        return departmentMapper.addDepartment(department);
    }

    @Override
    public boolean editDepartment(Department department) {
        return departmentMapper.editDepartment(department);
    }

    @Override
    public boolean deleteDepartment(Department department) {
        return departmentMapper.deleteDepartment(department);
    }
}
