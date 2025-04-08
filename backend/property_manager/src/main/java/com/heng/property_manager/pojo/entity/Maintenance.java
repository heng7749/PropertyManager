package com.heng.property_manager.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Maintenance extends BaseEntity {
    private Long id;
    private Long equipmentId;
    private Integer status;
    private Long maintainerId;
    private java.sql.Date maintainDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private java.sql.Date maintainTargetDate;
    private String remark;

}
