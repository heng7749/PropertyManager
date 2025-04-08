package com.heng.property_manager.mapper;

import com.heng.property_manager.pojo.entity.Parking;
import com.heng.property_manager.pojo.entity.Repair;
import com.heng.property_manager.pojo.vo.RepairVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface RepairMapper {
    List<RepairVo> searchRepairs(RepairVo repairVo);

    List<RepairVo> listUserRepairRecords(RepairVo repairVo);

    @Insert("insert into repair(house_id,type,phoneNumber,contents,photo,create_time) " +
            "values(#{houseId},#{type},#{phoneNumber},#{contents},#{photo},#{createTime})")
    boolean addRepair(Repair repair);

    boolean editRepair(Repair repair);

    @Update("update repair set is_deleted = 1 where id = #{id}")
    boolean deleteRepair(Long id);

    @Update("update repair set status=#{status},repairer_id=#{repairerId},update_time=#{updateTime},update_by=#{updateBy} where id=#{id}")
    boolean conductRepair(Repair repair);
}
