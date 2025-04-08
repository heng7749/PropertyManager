package com.heng.property_manager.service;

import com.heng.property_manager.pojo.entity.Purchase;
import com.heng.property_manager.pojo.vo.PurchaseVo;

import java.util.List;

public interface PurchaseService {
    List<PurchaseVo> searchPurchases(PurchaseVo purchaseVo);

    List<PurchaseVo> listUserApplyRecords(PurchaseVo purchaseVo);

    boolean addPurchase(Purchase purchase);

    boolean editPurchase(Purchase purchase);

    boolean conductPurchase(Purchase purchase);

    boolean deletePurchase(Purchase purchase);
}
