package com.heng.property_manager.service.Impl;

import com.heng.property_manager.mapper.HouseMapper;
import com.heng.property_manager.pojo.entity.House;
import com.heng.property_manager.pojo.entity.LoginUser;
import com.heng.property_manager.pojo.vo.HouseVo;
import com.heng.property_manager.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseServiceImpl implements HouseService {

    @Autowired
    private HouseMapper houseMapper;

    @Override
    public List<House> searchHouse(House house) {
        return houseMapper.searchHouse(house);
    }

    @Override
    public List<House> searchHouseByOwnerName(House house) {
        return houseMapper.searchHouseByOwnerName(house);
    }

    @Override
    public boolean addHouse(House house) {
        boolean is_exist = houseMapper.houseIsExist(house);
        if (is_exist) {
            return false;
        } else
            return houseMapper.addHouse(house);
    }

    @Override
    public boolean editHouse(House house) {
        return houseMapper.editHouse(house);
    }

    @Override
    public boolean deleteHouse(Long id) {
        return houseMapper.deleteHouse(id);
    }

    @Override
    public List<HouseVo> getOwnerHouses() {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return houseMapper.getHouseByOwnerId(principal.getUser().getId());
    }
}
