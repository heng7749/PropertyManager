package com.heng.property_manager.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Equipment extends BaseEntity{
    private Long id;
    private String name;
    private Integer type;
    private String model;
    private String deployPlace;
    private Long departmentId;
    private Integer status;
    private Date deployTime;
    private String purchasingTime;
    private String producer;
    private Integer repairsPeriod;
    private Integer replacePeriod;
    private String remark;
}
