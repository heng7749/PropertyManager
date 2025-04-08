package com.heng.property_manager.service.Impl;

import com.heng.property_manager.mapper.BillTypeMapper;
import com.heng.property_manager.mapper.BuildingMapper;
import com.heng.property_manager.pojo.entity.BillType;
import com.heng.property_manager.pojo.entity.LoginUser;
import com.heng.property_manager.pojo.vo.BillTypeVo;
import com.heng.property_manager.service.BillTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BillTypeServiceImpl implements BillTypeService {
    @Autowired
    private BillTypeMapper billTypeMapper;

    @Override
    public List<BillTypeVo> searchBillType(BillType billType) {
        return billTypeMapper.searchBillType(billType);
    }

    @Override
    public boolean addBillType(BillType billType) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        billType.setCreateBy(principal.getUser().getBindId());
        billType.setCreateTime(new Date());
        return billTypeMapper.addBillType(billType);
    }

    @Override
    public boolean editBillType(BillType billType) {
        return billTypeMapper.editBillType(billType);
    }

    @Override
    public boolean deleteBillType(BillType billType) {
        return billTypeMapper.deleteBillType(billType);
    }
}
