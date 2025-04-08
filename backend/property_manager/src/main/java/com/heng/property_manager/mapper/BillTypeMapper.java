package com.heng.property_manager.mapper;

import com.heng.property_manager.pojo.entity.BillType;
import com.heng.property_manager.pojo.vo.BillTypeVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BillTypeMapper {
    List<BillTypeVo> searchBillType(BillType billType);

    @Insert("insert into bill_type(name,unit_price,create_time,create_by) " +
            "values(#{name},#{unitPrice},#{createTime},#{createBy})")
    boolean addBillType(BillType billType);

    @Update("update bill_type set name=#{name},unit_price=#{unitPrice}where id=#{id}")
    boolean editBillType(BillType billType);

    @Update("update bill_type set is_deleted = 1 where id = #{id}")
    boolean deleteBillType(BillType billType);
}
