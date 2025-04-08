package com.heng.property_manager.service;

import com.heng.property_manager.pojo.entity.Building;
import com.heng.property_manager.pojo.vo.BuildingVo;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface BuildingService {
    List<BuildingVo> searchBuilding(Building building);

    boolean addBuilding(Building building) throws SQLIntegrityConstraintViolationException;

    boolean editBuilding(Building building);

    boolean deleteBuilding(Building building);
}
