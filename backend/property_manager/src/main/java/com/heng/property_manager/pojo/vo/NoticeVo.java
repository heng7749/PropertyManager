package com.heng.property_manager.pojo.vo;

import com.heng.property_manager.pojo.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoticeVo extends BaseEntity {
    private Long id;
    private Integer type;
    private String subject;
    private String contents;
    private Integer status;
    private String creator;
    private String conductor;
    private int userType;
    private Long departmentId;
}
