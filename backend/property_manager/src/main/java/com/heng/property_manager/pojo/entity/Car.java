package com.heng.property_manager.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Car {
    private Long id;
    private String carNumber;
    private String ownerName;
    private String phoneNumber;
    private Long ownerId;
    private Long stallId;
}
