package com.heng.property_manager.service;

import com.heng.property_manager.pojo.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> listEmployee();

    List<Employee> searchEmployee(Employee employee);

    List<Employee> searchEmployeeById(long parseLong);

    boolean addEmployee(Employee employee);

    boolean editEmployee(Employee employee);

    boolean deleteEmployee(Long id);
}
