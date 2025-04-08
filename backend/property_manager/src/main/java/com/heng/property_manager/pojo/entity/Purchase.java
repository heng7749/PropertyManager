package com.heng.property_manager.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Purchase extends BaseEntity{
    private Long id;
    private String name;
    private Integer type;
    private String model;
    private String producer;
    private Integer number;
    private String price;
    private Integer status;
    private String remark;
    private String feedback;
}
