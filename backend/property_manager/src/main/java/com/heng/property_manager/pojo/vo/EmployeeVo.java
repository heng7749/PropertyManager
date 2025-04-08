package com.heng.property_manager.pojo.vo;

import com.heng.property_manager.pojo.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmployeeVo extends BaseEntity {
    private Long id;
    private String name;
    private int gender;
    private Long departmentId;
    private String position;
    private String phoneNumber;
    private String email;
    private String birthday;
    private String identification;
    private String address;
    private float salary;
    private String department;
}
