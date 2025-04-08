package com.heng.property_manager.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Repair extends BaseEntity{
    private Long id;
    private String name;
    private Long houseId;
    private int type;
    private int status;
    private String phoneNumber;
    private String contents;
    private String photo;
    private Long repairerId;
    private String feedback;
}
