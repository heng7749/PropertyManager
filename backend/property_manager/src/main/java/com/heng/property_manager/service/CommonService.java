package com.heng.property_manager.service;

import com.heng.property_manager.pojo.entity.BillType;
import com.heng.property_manager.pojo.entity.Building;
import com.heng.property_manager.pojo.entity.Department;
import com.heng.property_manager.pojo.entity.ParkingLot;

import java.util.List;

public interface CommonService {
    List<Department> listDepartments();

    List<Building> listBuildings();

    List<ParkingLot> listParkingLots();

    List<BillType> listBillTypes();
}
