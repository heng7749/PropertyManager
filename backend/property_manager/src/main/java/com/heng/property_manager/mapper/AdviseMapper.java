package com.heng.property_manager.mapper;

import com.heng.property_manager.pojo.entity.Advise;
import com.heng.property_manager.pojo.vo.AdviseVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AdviseMapper {
    List<AdviseVo> searchAdvice(Advise advise);

    List<AdviseVo> listUserAdvice(Advise advise);

    @Insert("insert into advise(owner_id,type,subject,contents,create_time) " +
            "values(#{ownerId},#{type},#{subject},#{contents},#{createTime})")
    boolean addAdvice(Advise advise);

    @Update("update advise set type=#{type},subject=#{subject},contents=#{contents} where id=#{id}")
    boolean editAdvice(Advise advise);

    @Update("update advise set status=#{status},feedback=#{feedback},update_time=#{updateTime},update_by=#{updateBy} where id=#{id}")
    boolean conductAdvice(Advise advise);

    @Update("update advise set is_deleted = 1 where id = #{id}")
    boolean deleteAdvice(Advise advise);
}
