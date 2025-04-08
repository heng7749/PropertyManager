package com.heng.property_manager.service.Impl;

import com.heng.property_manager.mapper.OwnerMapper;
import com.heng.property_manager.pojo.entity.House;
import com.heng.property_manager.pojo.entity.Owner;
import com.heng.property_manager.pojo.vo.OwnerVo;
import com.heng.property_manager.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerServiceImpl implements OwnerService {
    @Autowired
    private OwnerMapper ownerMapper;

    @Override
    public List<Owner> searchByName(Owner owner) {
        List<Owner> owners = ownerMapper.searchByName(owner);
        return owners;
    }

    @Override
    public List<OwnerVo> searchByHouse(OwnerVo ownerVo) {
        return ownerMapper.searchByHouse(ownerVo);
    }

    @Override
    public boolean addOwner(Owner owner) {
        boolean is_exist = ownerMapper.ownerIsExist(owner);
        if (is_exist) {
            return false;
        } else
            return ownerMapper.addOwner(owner);
    }

    @Override
    public boolean editOwner(Owner owner) {
        return ownerMapper.editOwner(owner);
    }

    @Override
    public boolean deleteOwner(Long id) {
        return ownerMapper.deleteOwner(id);
    }
}
