package com.heng.property_manager.mapper;

import com.heng.property_manager.pojo.entity.Advise;
import com.heng.property_manager.pojo.entity.Building;
import com.heng.property_manager.pojo.vo.AdviseVo;
import com.heng.property_manager.pojo.vo.BuildingVo;
import org.apache.ibatis.annotations.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Mapper
public interface BuildingMapper {
    List<BuildingVo> searchBuilding(Building building);

    @Insert("insert into building(name,units,floors,create_time,create_by) " +
            "values(#{name},#{units},#{floors},#{createTime},#{createBy})")
    boolean addBuilding(Building building);

    @Update("update building set name=#{name},units=#{units},floors=#{floors} where id=#{id}")
    boolean editBuilding(Building building);

    @Update("update building set is_deleted = 1 where id = #{id}")
    boolean deleteBuilding(Building building);
}
