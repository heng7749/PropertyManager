package com.heng.property_manager.service;

import com.heng.property_manager.pojo.entity.House;
import com.heng.property_manager.pojo.entity.Owner;
import com.heng.property_manager.pojo.vo.OwnerVo;

import java.util.List;

public interface OwnerService {
    List<Owner> searchByName(Owner owner);
    List<OwnerVo> searchByHouse(OwnerVo ownerVo);

    boolean addOwner(Owner owner);

    boolean editOwner(Owner owner);

    boolean deleteOwner(Long id);
}
