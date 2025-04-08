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
public class CarVo extends BaseEntity {
    private Long id;
    private String carNumber;
    private String ownerName;
    private String phoneNumber;
    private Long ownerId;
    private Long stallId;
    private int delMark;
}
