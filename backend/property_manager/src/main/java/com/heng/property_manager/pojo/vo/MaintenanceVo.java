package com.heng.property_manager.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.heng.property_manager.pojo.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MaintenanceVo extends BaseEntity {
    private Long id;
    private Long equipmentId;
    private Integer status;
    private Long maintainerId;
    @JsonFormat(pattern="yyyy-MM-dd")
    private java.sql.Date maintainDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date maintainTargetDate;
    private String remark;

    private String maintainer;

    private String equipmentName;
    private Integer type;
    private String deployPlace;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date deployTime;
    private Long departmentId;
    private String departmentName;
    private String model;
    private String purchasingTime;
    private String producer;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date replaceTargetDate;
    private Integer repairsPeriod;
    private Integer replacePeriod;
}
