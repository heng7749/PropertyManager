package com.heng.property_manager.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Duty extends BaseEntity {
    private Long id;
    private Long employeeId;
    private String shift;
    private String job;
    private String attendedDate;
}
