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
public class EquipmentVo extends BaseEntity {
    private Long id;
    private String name;
    private Integer type;
    private String model;
    private String deployPlace;
    private Long departmentId;
    private Integer status;
    private String deployTime;
    private String purchasingTime;
    private String producer;
    private Integer repairsPeriod;
    private Integer replacePeriod;
    private String remark;
    private String creator;
    private String departmentName;
}
