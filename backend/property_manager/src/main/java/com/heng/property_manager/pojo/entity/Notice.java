package com.heng.property_manager.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Notice extends BaseEntity{
    private Long id;
    private Integer type;
    private String subject;
    private String contents;
    private Integer status;
}
