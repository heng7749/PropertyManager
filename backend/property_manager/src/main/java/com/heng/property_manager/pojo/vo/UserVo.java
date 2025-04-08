package com.heng.property_manager.pojo.vo;

import com.heng.property_manager.pojo.entity.BaseEntity;
import com.heng.property_manager.pojo.entity.Employee;
import com.heng.property_manager.pojo.entity.Owner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserVo extends BaseEntity {
    private Long id;
    private String name;
    private String password;
    private Integer type;
    private Long bindId;
    private String avatar;
    private Owner owner;
    private EmployeeVo employeeVo;
    private Long departmentId;
    private Long surId;
}
