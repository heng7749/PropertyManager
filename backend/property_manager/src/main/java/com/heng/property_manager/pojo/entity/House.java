package com.heng.property_manager.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class House extends BaseEntity{
    private Long id;
    private Long ownerId;
    private Long buildingId;
    private Integer unit;
    private Integer floor;
    private String name;
    private String houseType;
    private float floorSpace;
    private int status;
    private String remark;
    private Owner owner;
}
