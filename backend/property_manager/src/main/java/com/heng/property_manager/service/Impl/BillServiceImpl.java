package com.heng.property_manager.service.Impl;

import com.heng.property_manager.mapper.BillMapper;
import com.heng.property_manager.pojo.entity.Bill;
import com.heng.property_manager.pojo.entity.BillType;
import com.heng.property_manager.pojo.entity.LoginUser;
import com.heng.property_manager.pojo.vo.BillVo;
import com.heng.property_manager.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    private BillMapper billMapper;

    @Override
    public List<BillVo> searchBill(BillVo billVo) {
        return billMapper.searchBill(billVo);
    }

    @Override
    public boolean addBill(Bill bill) {
        BillType billType = billMapper.searchBillTypeById(bill.getBillTypeId());
        // 金额计算
        float money = bill.getCount() * billType.getUnitPrice();
        bill.setMoney(money);
        // 操作用户与时间
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        bill.setCreateBy(principal.getUser().getId());
        bill.setCreateTime(new Date());
        // 上次抄表时间
        Date date = billMapper.searchLastTime(bill);
        bill.setStartTime(date);
        return billMapper.addBill(bill);
    }

    @Override
    public boolean editBill(Bill bill) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        bill.setUpdateBy(principal.getUser().getId());
        bill.setUpdateTime(new Date());
        return billMapper.editBill(bill);
    }

    @Override
    public boolean deleteBill(Bill bill) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        bill.setUpdateBy(principal.getUser().getId());
        bill.setUpdateTime(new Date());
        return billMapper.deleteBill(bill);
    }

    @Override
    public List<BillVo> myBills(BillVo billVo) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        billVo.setOwnerId(principal.getUser().getId());
        return billMapper.listUserBills(billVo);
    }
}
