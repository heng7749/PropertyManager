package com.heng.property_manager.service.Impl;

import com.heng.property_manager.mapper.EmployeeMapper;
import com.heng.property_manager.pojo.entity.Employee;
import com.heng.property_manager.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<Employee> listEmployee() {
        return employeeMapper.listEmployee();
    }

    @Override
    public List<Employee> searchEmployee(Employee employee) {
        return employeeMapper.searchEmployee(employee);
    }

    @Override
    public List<Employee> searchEmployeeById(long employeeId) {
        return employeeMapper.searchEmployeeById(employeeId);
    }

    @Override
    public boolean addEmployee(Employee employee) {
        return employeeMapper.addEmployee(employee);
    }

    @Override
    public boolean editEmployee(Employee employee) {
        return employeeMapper.editEmployee(employee);
    }

    @Override
    public boolean deleteEmployee(Long id) {
        return employeeMapper.deleteEmployee(id);
    }
}
