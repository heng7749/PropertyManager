package com.heng.property_manager.service;

import com.heng.property_manager.pojo.entity.House;
import com.heng.property_manager.pojo.vo.HouseVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface HouseService {
    List<House> searchHouse(House House);

    List<House> searchHouseByOwnerName(House house);

    boolean addHouse(House house);

    boolean editHouse(House house);

    boolean deleteHouse(Long id);

    List<HouseVo> getOwnerHouses();
}
