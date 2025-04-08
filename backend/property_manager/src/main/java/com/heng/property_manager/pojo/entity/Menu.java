package com.heng.property_manager.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Menu extends BaseEntity{
    private Long id;
    private String name;
    private String icon;
    private Long parentId;
    private int orderNum;
    private String path;
    private String component;
    private char menuType;
    private String perms;
    private List<Menu> children;
}
