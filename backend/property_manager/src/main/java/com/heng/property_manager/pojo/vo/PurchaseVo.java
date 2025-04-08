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
public class PurchaseVo extends BaseEntity {
    private Long id;
    private String name;
    private Integer type;
    private String model;
    private String producer;
    private Integer number;
    private String price;
    private Integer status;
    private String remark;
    private Long departmentId;
    private String departmentName;
    private Long applicantId;
    private String applicant;
    private String conductor;
    private String feedback;
}
