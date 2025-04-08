package com.heng.property_manager.pojo.entity;

import com.heng.property_manager.pojo.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Owner extends BaseEntity {
    private Long id;
    private String name;
    private int gender;
    private String phoneNumber;
    private String email;
    private String birthday;
    private String identification;
}
