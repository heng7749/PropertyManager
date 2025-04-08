package com.heng.property_manager.mapper;

import com.heng.property_manager.pojo.entity.Purchase;
import com.heng.property_manager.pojo.vo.PurchaseVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PurchaseMapper {
    List<PurchaseVo> searchPurchases(PurchaseVo purchaseVo);

    List<PurchaseVo> listUserApplyRecords(PurchaseVo purchaseVo);

    @Insert("insert into purchase(name,type,model,producer,price,number,create_by,create_time,remark) " +
            "values(#{name},#{type},#{model},#{producer},#{price},#{number},#{createBy},#{createTime},#{remark})")
    boolean addPurchase(Purchase purchase);

    @Update("update purchase set name=#{name},type=#{type},model=#{model},producer=#{producer},price=#{price},number=#{number},remark=#{remark} where id=#{id}")
    boolean editPurchase(Purchase purchase);

    @Update("update purchase set status=#{status},feedback=#{feedback},update_time=#{updateTime},update_by=#{updateBy} where id=#{id}")
    boolean conductPurchase(Purchase purchase);

    @Update("update purchase set is_deleted = 1 where id = #{id}")
    boolean deletePurchase(Purchase purchase);
}
