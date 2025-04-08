package com.heng.property_manager.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Stall extends BaseEntity{
    private Long id;
    private Long parkingLotId;
    private int area;
    private String stallNumber;
    private int status;
    private int condition;
    private Long ownerId;
}
