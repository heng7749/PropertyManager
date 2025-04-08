package com.heng.property_manager.service.Impl;

import com.heng.property_manager.mapper.CommonMapper;
import com.heng.property_manager.pojo.entity.BillType;
import com.heng.property_manager.pojo.entity.Building;
import com.heng.property_manager.pojo.entity.Department;
import com.heng.property_manager.pojo.entity.ParkingLot;
import com.heng.property_manager.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private CommonMapper commonMapper;

    @Override
    public List<Department> listDepartments() {
        return commonMapper.listDepartments();
    }

    @Override
    public List<Building> listBuildings() {
        return commonMapper.listBuildings();
    }

    @Override
    public List<ParkingLot> listParkingLots() {
        return commonMapper.listParkingLots();
    }

    @Override
    public List<BillType> listBillTypes() {
        return commonMapper.listBillTypes();
    }
}
