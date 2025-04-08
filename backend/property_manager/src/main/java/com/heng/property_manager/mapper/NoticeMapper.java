package com.heng.property_manager.mapper;

import com.heng.property_manager.pojo.entity.Notice;
import com.heng.property_manager.pojo.vo.NoticeVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface NoticeMapper {
    @Insert("insert into notice(type,subject,contents,status,create_by) values (#{type},#{subject},#{contents},#{status},#{createBy})")
    boolean publish(Notice notice);

    List<NoticeVo> searchNotices(Notice notice);

    List<NoticeVo> searchUserNotices(NoticeVo noticeVo);

    List<NoticeVo> findNoticeById(Long noticeId);

    @Update("update notice set subject=#{subject},type=#{type},contents=#{contents},status=#{status},update_time=#{updateTime},update_by=#{updateBy} where id=#{id}")
    boolean editNotice(Notice notice);

    @Update("update notice set is_deleted=1,update_by=#{updateBy},update_time=#{updateTime} where id=#{id}")
    boolean deleteNotice(Notice notice);
}
