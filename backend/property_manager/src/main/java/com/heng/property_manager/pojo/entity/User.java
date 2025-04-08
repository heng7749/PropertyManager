package com.heng.property_manager.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User extends BaseEntity{
    private Long id;
    private String name;
    private String password;
    private Integer type;
    private Long bindId;
    private String avatar;
}
