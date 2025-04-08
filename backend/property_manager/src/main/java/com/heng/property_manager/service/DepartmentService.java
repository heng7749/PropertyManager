package com.heng.property_manager.service;

import com.heng.property_manager.pojo.entity.Department;
import com.heng.property_manager.pojo.vo.DepartmentVo;

import java.util.List;

public interface DepartmentService {
    List<DepartmentVo> searchDepartment(Department department);

    boolean addDepartment(Department department);

    boolean editDepartment(Department department);

    boolean deleteDepartment(Department department);
}
