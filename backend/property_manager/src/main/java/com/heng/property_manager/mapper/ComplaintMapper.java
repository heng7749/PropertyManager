package com.heng.property_manager.mapper;

import com.heng.property_manager.pojo.entity.Complaint;
import com.heng.property_manager.pojo.vo.ComplaintVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ComplaintMapper {
    List<ComplaintVo> searchComplaints(Complaint complaint);

    List<ComplaintVo> listUserComplaints(Complaint complaint);

    @Insert("insert into complaint(owner_id,type,subject,contents,phoneNumber,create_time) " +
            "values(#{ownerId},#{type},#{subject},#{contents},#{phoneNumber},#{createTime})")
    boolean addComplaint(Complaint complaint);

    @Update("update complaint set type=#{type},subject=#{subject},contents=#{contents},phoneNumber=#{phoneNumber} where id=#{id}")
    boolean editComplaint(Complaint complaint);

    @Update("update complaint set status=#{status},feedback=#{feedback},update_time=#{updateTime},update_by=#{updateBy} where id=#{id}")
    boolean conductComplaint(Complaint complaint);

    @Update("update complaint set is_deleted = 1 where id = #{id}")
    boolean deleteComplaint(Complaint complaint);
}
