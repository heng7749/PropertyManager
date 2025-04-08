package com.heng.property_manager.mapper;

import com.heng.property_manager.pojo.entity.House;
import com.heng.property_manager.pojo.entity.Owner;
import com.heng.property_manager.pojo.vo.OwnerVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface OwnerMapper {
    @Select("select * from  owner where is_deleted = 0 and name like concat('%',#{name},'%') ORDER BY id DESC")
    List<Owner> searchByName(Owner owner);

    List<OwnerVo> searchByHouse(OwnerVo ownerVo);

    @Insert("insert into owner(name,gender,phoneNumber,email,birthday,identification) " +
            "values(#{name},#{gender},#{phoneNumber},#{email},#{birthday},#{identification})")
    boolean addOwner(Owner owner);

    @Update("update owner set name=#{name},gender=#{gender},phoneNumber=#{phoneNumber},email=#{email},birthday=#{birthday},identification=#{identification} where id=#{id}")
    boolean editOwner(Owner owner);

    @Update("update owner set is_deleted = 1 where id = #{id}")
    boolean deleteOwner(Long id);

    @Select("select count(1) from owner where name=#{name} and identification=#{identification} and is_deleted = 0")
    boolean ownerIsExist(Owner owner);
}
