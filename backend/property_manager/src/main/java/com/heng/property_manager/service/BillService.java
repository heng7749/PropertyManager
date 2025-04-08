package com.heng.property_manager.service;

import com.heng.property_manager.pojo.entity.Bill;
import com.heng.property_manager.pojo.vo.BillVo;

import java.util.List;

public interface BillService {
    List<BillVo> searchBill(BillVo billVo);

    boolean addBill(Bill bill);

    boolean editBill(Bill bill);

    boolean deleteBill(Bill bill);

    List<BillVo> myBills(BillVo billVo);
}
