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
public class BillVo extends BaseEntity {
    private Long id;
    private Long houseId;
    private Long billTypeId;
    private float count;
    private float money;
    private int status;
    private String startTime;
    private String endTime;

    private Long ownerId;
    private String ownerName;
    private String phoneNumber;
    private String email;
    private String houseName;
    private Long buildingId;
    private Integer unit;
    private Integer floor;
    private String address;

    private String billTypeName;
    private float unitPrice;
}
