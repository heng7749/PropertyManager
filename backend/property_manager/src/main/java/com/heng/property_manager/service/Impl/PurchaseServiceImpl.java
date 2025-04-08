package com.heng.property_manager.service.Impl;

import com.heng.property_manager.mapper.PurchaseMapper;
import com.heng.property_manager.mapper.RepairMapper;
import com.heng.property_manager.pojo.entity.LoginUser;
import com.heng.property_manager.pojo.entity.Purchase;
import com.heng.property_manager.pojo.vo.PurchaseVo;
import com.heng.property_manager.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    private PurchaseMapper purchaseMapper;

    @Override
    public List<PurchaseVo> searchPurchases(PurchaseVo purchaseVo) {
        return purchaseMapper.searchPurchases(purchaseVo);
    }

    @Override
    public List<PurchaseVo> listUserApplyRecords(PurchaseVo purchaseVo) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        purchaseVo.setApplicantId(principal.getUser().getId());
        return purchaseMapper.listUserApplyRecords(purchaseVo);
    }

    @Override
    public boolean addPurchase(Purchase purchase) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 获取员工id
        purchase.setCreateBy(principal.getUser().getBindId());
        purchase.setCreateTime(new Date());
        return purchaseMapper.addPurchase(purchase);
    }

    @Override
    public boolean editPurchase(Purchase purchase) {
        return purchaseMapper.editPurchase(purchase);
    }

    @Override
    public boolean conductPurchase(Purchase purchase) {
        if (purchase.getStatus() == 0) {
            purchase.setFeedback("");
            purchase.setUpdateTime(null);
        } else {
            LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // 获取操作者id
            purchase.setUpdateBy(principal.getUser().getBindId());
            purchase.setUpdateTime(new Date());
        }
        return purchaseMapper.conductPurchase(purchase);
    }

    @Override
    public boolean deletePurchase(Purchase purchase) {
        return purchaseMapper.deletePurchase(purchase);
    }
}
