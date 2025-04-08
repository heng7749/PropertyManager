package com.heng.property_manager.mapper;

import com.heng.property_manager.pojo.entity.BillType;
import com.heng.property_manager.pojo.entity.Building;
import com.heng.property_manager.pojo.entity.Department;
import com.heng.property_manager.pojo.entity.ParkingLot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommonMapper {
    @Select("select * from department where is_deleted = 0")
    List<Department> listDepartments();

    @Select("select * from building where is_deleted = 0")
    List<Building> listBuildings();

    @Select("select * from parking_lot where is_deleted = 0")
    List<ParkingLot> listParkingLots();

    @Select("select * from bill_type where is_deleted = 0")
    List<BillType> listBillTypes();
}
