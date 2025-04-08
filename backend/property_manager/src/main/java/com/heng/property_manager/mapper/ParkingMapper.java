package com.heng.property_manager.mapper;

import com.heng.property_manager.pojo.entity.Car;
import com.heng.property_manager.pojo.entity.Parking;
import com.heng.property_manager.pojo.entity.Stall;
import com.heng.property_manager.pojo.vo.CarVo;
import com.heng.property_manager.pojo.vo.ParkingVo;
import com.heng.property_manager.pojo.vo.StallVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ParkingMapper {
    List<StallVo> searchStall(Stall stall);

    List<ParkingVo> searchParkingRecords(ParkingVo parkingVo);

    @Insert("insert into stall(parking_lot_id,area,stallNumber) " +
            "values(#{parkingLotId},#{area},#{stallNumber})")
    boolean addStall(Stall stall);

    @Update("update stall set status=#{status},owner_id=#{ownerId} where id=#{id}")
    boolean editStall(StallVo stallVo);

    @Update("update stall set is_deleted = 1 where id = #{id}")
    boolean deleteStall(Stall stall);

    @Select("select count(1) from stall where parking_lot_id=#{parkingLotId} and area=#{area} and stallNumber=#{stallNumber} and is_deleted = 0")
    boolean stallIsExist(Stall stall);

    @Insert("insert into car(carNumber,ownerName,phoneNumber,owner_id,stall_id) values(#{carNumber},#{ownerName},#{phoneNumber},#{ownerId},#{stallId}) ")
    boolean addOwnerCar(CarVo carVo);

    @Insert("insert into car(carNumber,ownerName,phoneNumber) values(#{carNumber},#{ownerName},#{phoneNumber}) ")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    boolean addCar(Car car);

    @Select("select * from car where is_deleted = 0 and stall_id = #{id}")
    List<Car> getCarByStallId(Long id);

    @Update("update car set stall_id = null where id = #{id}")
    boolean deBindCarFromStall(Long id);

    @Select("select * from car where carNumber=#{carNumber} limit 1")
    CarVo searchCarByCarNumber(String carNumber);

    @Select("select * from car where is_deleted = 0 and owner_id = #{ownerId}")
    List<Car> getOwnerCars(Long ownerId);

    @Update("update car set stall_id = #{stallId} where id = #{id}")
    boolean bindCarToStall(CarVo car);

    @Insert("insert into parking(car_id,stall_id,start_time,bill_type_id,create_time,create_by) " +
            "values(#{carId},#{stallId},#{billTypeId},#{createTime},#{createBy})")
    boolean addParking(ParkingVo parkingVo);

    @Update("update parking set start_time=#{startTime}, end_time=#{endTime},status=#{status},bill_type_id=#{billTypeId},money=#{money},update_time=#{updateTime}, update_by=#{updateBy} where id=#{id}")
    boolean editParking(Parking parking);

    @Update("update parking set is_deleted = 1, update_time=#{updateTime}, update_by=#{updateBy} where id = #{id}")
    boolean deleteParking(Parking parking);

    @Select("SELECT * FROM stall s \n" +
            "LEFT JOIN (SELECT stall_id FROM parking WHERE stall_id IS NOT NULL AND end_time IS NULL) p ON s.id = p.stall_id\n" +
            "WHERE stall_id IS NULL")
    List<Stall> getFreeStall();
}
