package com.heng.property_manager.pojo.vo;

import com.heng.property_manager.pojo.entity.BaseEntity;
import com.heng.property_manager.pojo.entity.Owner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HouseVo extends BaseEntity {
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
    private String buildingName;
}
