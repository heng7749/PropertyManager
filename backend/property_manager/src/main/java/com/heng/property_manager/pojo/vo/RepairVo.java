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
public class RepairVo extends BaseEntity {
    private Long id;
    private Long houseId;
    private Integer type;
    private String phoneNumber;
    private String contents;
    private String photo;
    private Integer status;
    private Long repairerId;
    private String feedback;

    private String repairer;
    private Long buildingId;
    private Integer unit;
    private Integer floor;
    private String houseName;
    private Long ownerId;
    private String ownerName;
    private String buildingName;

}
