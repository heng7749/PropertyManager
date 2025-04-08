package com.heng.property_manager.service;

import com.heng.property_manager.pojo.entity.BillType;
import com.heng.property_manager.pojo.vo.BillTypeVo;

import java.util.List;

public interface BillTypeService {
    List<BillTypeVo> searchBillType(BillType billType);

    boolean addBillType(BillType billType);

    boolean editBillType(BillType billType);

    boolean deleteBillType(BillType billType);
}
