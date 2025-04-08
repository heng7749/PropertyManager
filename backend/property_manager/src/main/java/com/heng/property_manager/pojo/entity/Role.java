package com.heng.property_manager.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Role extends BaseEntity {
    private Long id;
    private String name;
    private String code;
    private String remark;
}
