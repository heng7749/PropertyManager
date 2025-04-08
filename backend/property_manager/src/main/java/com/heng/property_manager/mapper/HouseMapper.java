package com.heng.property_manager.mapper;

import com.heng.property_manager.pojo.entity.House;
import com.heng.property_manager.pojo.vo.HouseVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface HouseMapper {
    List<House> searchHouse(House house);
    List<House> searchHouseByOwnerName(House house);

    @Insert("insert into house(owner_id,building_id,unit,floor,name,houseType,floorSpace,status,remark) " +
                "values(#{ownerId},#{buildingId},#{unit},#{floor},#{name},#{houseType},#{floorSpace},#{status},#{remark})")
    boolean addHouse(House house);

    @Update("update house set owner_id=#{ownerId},name=#{name},houseType=#{houseType},floorSpace=#{floorSpace},status=#{status},remark=#{remark} where id=#{id}")
    boolean editHouse(House house);

    @Update("update house set is_deleted = 1 where id = #{id}")
    boolean deleteHouse(Long id);

    @Select("select count(1) from hosue where building_id=#{buildingId} and unit=#{unit} and floor=#{floor} and name=#{name} and is_deleted = 0")
    boolean houseIsExist(House house);

    @Select("SELECT h.*,b.name buildingName from sys_user u\n" +
                "LEFT JOIN house h ON u.bind_id = h.owner_id\n" +
                "LEFT JOIN building b ON h.building_id = b.id\n" +
            "WHERE u.type = 0 and u.id = #{ownerId} and u.is_deleted = 0")
    List<HouseVo> getHouseByOwnerId(Long ownerId);
}
