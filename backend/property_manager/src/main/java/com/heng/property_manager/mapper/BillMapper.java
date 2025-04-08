package com.heng.property_manager.mapper;

import com.heng.property_manager.pojo.entity.Bill;
import com.heng.property_manager.pojo.entity.BillType;
import com.heng.property_manager.pojo.vo.BillVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

@Mapper
public interface BillMapper {
    List<BillVo> searchBill(BillVo billVo);

    @Insert("insert into bill(house_id,bill_type_id,count,money,start_time,end_time,create_by,create_time) " +
            "values(#{houseId},#{billTypeId},#{count},#{money},#{startTime},#{endTime},#{createBy},#{createTime})")
    boolean addBill(Bill bill);

    @Update("update bill set status=#{status}, update_time=#{updateTime}, update_by=#{updateBy} where id=#{id}")
    boolean editBill(Bill bill);

    @Update("update bill set is_deleted = 1, update_time=#{updateTime}, update_by=#{updateBy} where id = #{id}")
    boolean deleteBill(Bill bill);

    @Select("select * from bill_type where is_deleted = 0 and id=#{id}")
    BillType searchBillTypeById(Long id);

    @Select("select end_time from bill where is_deleted = 0 and  house_id = #{houseId} and bill_type_id = #{billTypeId} order by end_time desc")
    Date searchLastTime(Bill bill);

    List<BillVo> listUserBills(BillVo billVo);
}
