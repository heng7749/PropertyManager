package com.heng.property_manager.mapper;

import com.heng.property_manager.pojo.entity.Employee;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    @Select("select * from employee where is_deleted = 0 ORDER BY id desc")
    List<Employee> listEmployee();

    List<Employee> searchEmployee(Employee employee);

    @Select("select * from employee where id=#{employeeId} and is_deleted = 0 limit 1")
    List<Employee> searchEmployeeById(long employeeId);

    @Insert("insert into employee(name,gender,department_id,position,phoneNumber,email,birthday,identification,address,salary) " +
            "values(#{name},#{gender},#{departmentId},#{position},#{phoneNumber},#{email},#{birthday},#{identification},#{address},#{salary})")
    boolean addEmployee(Employee employee);

    @Update("update employee set name=#{name},gender=#{gender},department_id=#{departmentId},position=#{position},phoneNumber=#{phoneNumber},email=#{email},birthday=#{birthday},identification=#{identification},address=#{address},salary=#{salary} where id=#{id}")
    boolean editEmployee(Employee employee);

    @Update("update employee set is_deleted = 1 where id = #{id}")
    boolean deleteEmployee(Long id);
}