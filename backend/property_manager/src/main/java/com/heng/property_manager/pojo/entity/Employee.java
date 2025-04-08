package com.heng.property_manager.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee extends BaseEntity{
    private Long id;
    private String name;
    private int gender;
    private Long departmentId;
    private String position;
    private String phoneNumber;
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String identification;
    private String address;
    private float salary;
}
