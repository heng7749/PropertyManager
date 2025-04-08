package com.heng.property_manager.service.Impl;

import com.heng.property_manager.service.BuildingService;
import com.heng.property_manager.mapper.BuildingMapper;
import com.heng.property_manager.pojo.entity.Building;
import com.heng.property_manager.pojo.entity.LoginUser;
import com.heng.property_manager.pojo.vo.BuildingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BuildingServiceImpl implements BuildingService {
    @Autowired
    private BuildingMapper buildingMapper;

    @Override
    public List<BuildingVo> searchBuilding(Building building) {
        return buildingMapper.searchBuilding(building);
    }

    @Override
    public boolean addBuilding(Building building) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        building.setCreateBy(principal.getUser().getBindId());
        building.setCreateTime(new Date());
        return buildingMapper.addBuilding(building);
    }

    @Override
    public boolean editBuilding(Building building) {
        return buildingMapper.editBuilding(building);
    }

    @Override
    public boolean deleteBuilding(Building building) {
        return buildingMapper.deleteBuilding(building);
    }
}
