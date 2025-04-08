package com.heng.property_manager.pojo.vo;

import com.heng.property_manager.pojo.entity.Car;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StallVo {
    private Long id;
    private Long parkingLotId;
    private int area;
    private String stallNumber;
    private int status;
    private int condition;
    private Long ownerId;
    private String ownerName;
    private String phoneNumber;
    private String parkingLotName;
    private List<CarVo> cars;
}
