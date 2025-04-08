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
public class AdviseVo extends BaseEntity {
    private Long id;
    private Long ownerId;
    private Integer type;
    private String subject;
    private String contents;
    private Integer status;
    private String feedback;

    private String ownerName;
    private String address;
    private String conductor;
}
