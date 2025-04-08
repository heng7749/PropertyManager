package com.heng.property_manager.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.heng.property_manager.pojo.entity.BaseEntity;
import com.heng.property_manager.pojo.entity.Car;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ParkingVo extends BaseEntity{
    private Long id;
    private Long carId;
    private Long stallId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    private String endTime;
    private int status;
    private Long billTypeId;
    private float money;
    private String remark;
    private String carNumber;
    private String ownerName;
    private String phoneNumber;
    private Long ownerId;
    private Long parkingLotId;
    private String parkingLotName;
    private int area;
    private String stallNumber;
}
