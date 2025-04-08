package com.heng.property_manager.service;

import com.heng.property_manager.pojo.entity.Car;
import com.heng.property_manager.pojo.entity.Parking;
import com.heng.property_manager.pojo.entity.Stall;
import com.heng.property_manager.pojo.vo.ParkingVo;
import com.heng.property_manager.pojo.vo.StallVo;

import java.util.List;

public interface ParkingService {
    List<StallVo> searchStall(Stall stall);

    List<ParkingVo> searchParkingRecords(ParkingVo parkingVo);

    boolean addStall(Stall stall);

    boolean editStall(StallVo stallVo);

    boolean deleteStall(Stall stall);

    List<Car>  getStallBindCars(Stall stall);

    boolean addParking(ParkingVo parkingVo);

    boolean editParking(Parking parking);

    boolean deleteParking(Parking parking);

    List<Stall> getFreeStall();
}
