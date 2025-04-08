package com.heng.property_manager.service.Impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.heng.property_manager.mapper.BillMapper;
import com.heng.property_manager.mapper.HouseMapper;
import com.heng.property_manager.mapper.ParkingMapper;
import com.heng.property_manager.pojo.entity.*;
import com.heng.property_manager.pojo.vo.CarVo;
import com.heng.property_manager.pojo.vo.ParkingVo;
import com.heng.property_manager.pojo.vo.StallVo;
import com.heng.property_manager.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@Service
public class ParkingServiceImpl implements ParkingService {

    @Autowired
    private ParkingMapper parkingMapper;

    @Autowired
    private BillMapper billMapper;

    @Override
    public List<StallVo> searchStall(Stall stall) {
        return parkingMapper.searchStall(stall);
    }

    @Override
    public List<ParkingVo> searchParkingRecords(ParkingVo parkingVo) {
        return parkingMapper.searchParkingRecords(parkingVo);
    }

    @Override
    public boolean addStall(Stall stall) {
        boolean is_exist = parkingMapper.stallIsExist(stall);
        if (is_exist) {
            return false;
        } else
            return parkingMapper.addStall(stall);
    }

    @Override
    public boolean editStall(StallVo stallVo) {
        boolean is_succeed = true;
        List<CarVo> cars = stallVo.getCars();
        for (CarVo car : cars) {
            // 解除绑定
            if(car.getDelMark() == 1) {
                if (!parkingMapper.deBindCarFromStall(car.getId())) {
                    is_succeed = false;
                }
            } else if (car.getId() == null) {   //添加绑定
                CarVo temp = parkingMapper.searchCarByCarNumber(car.getCarNumber());
                if (temp == null) {
                    car.setOwnerId(stallVo.getOwnerId());
                    car.setPhoneNumber(stallVo.getPhoneNumber());
                    car.setStallId(stallVo.getId());
                    // 添加车辆信息
                    if (!parkingMapper.addOwnerCar(car)){
                        is_succeed = false;
                    }
                } else {
                    // 绑定已有车辆
                    temp.setStallId(stallVo.getId());
                    if (!parkingMapper.bindCarToStall(temp)) {
                        is_succeed = false;
                    }
                }
            }
        }
        if (!parkingMapper.editStall(stallVo)) {
            is_succeed = false;
        }
        return is_succeed;
    }

    @Override
    public boolean deleteStall(Stall stall) {
        return parkingMapper.deleteStall(stall);
    }

    @Override
    public List<Car> getStallBindCars(Stall stall) {
        return parkingMapper.getCarByStallId(stall.getId());
    }

    @Override
    public boolean addParking(ParkingVo parkingVo) {
        CarVo carVo = parkingMapper.searchCarByCarNumber(parkingVo.getCarNumber());
        if (carVo != null) {
            parkingVo.setCarId(carVo.getId());
        } else {    //添加车辆
            Car car = new Car();
            car.setCarNumber(parkingVo.getCarNumber());
            car.setOwnerName(parkingVo.getOwnerName());
            car.setPhoneNumber(parkingVo.getPhoneNumber());
            parkingMapper.addCar(car);
            parkingVo.setCarId(car.getId());
        }
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        parkingVo.setCreateBy(principal.getUser().getId());
        parkingVo.setCreateTime(new Date());
        parkingVo.setBillTypeId(4l);
        parkingVo.setStartTime(new Date());
        return parkingMapper.addParking(parkingVo);
    }

    @Override
    public boolean editParking(Parking parking) {
        BillType billType = billMapper.searchBillTypeById(parking.getBillTypeId());
        if (parking.getStartTime()!=null && parking.getEndTime()!=null) {
            // 时间差(小时)
            long between = DateUtil.between(parking.getStartTime(), parking.getEndTime(), DateUnit.MINUTE);
            long diff = between / 60;
            if (between%60 >= 30) {
                diff++;
            }
            // 金额计算
            float money = diff * billType.getUnitPrice();
            parking.setMoney(money);
        } else {
            parking.setMoney(0);
        }
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        parking.setUpdateBy(principal.getUser().getId());
        parking.setUpdateTime(new Date());
        return parkingMapper.editParking(parking);
    }

    @Override
    public boolean deleteParking(Parking parking) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        parking.setUpdateBy(principal.getUser().getId());
        parking.setUpdateTime(new Date());
        return parkingMapper.deleteParking(parking);
    }

    @Override
    public List<Stall> getFreeStall() {
        return parkingMapper.getFreeStall();
    }
}
