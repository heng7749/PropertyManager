package com.heng.property_manager.service;

import com.heng.property_manager.pojo.entity.Notice;
import com.heng.property_manager.pojo.vo.NoticeVo;

import java.util.List;

public interface NoticeService {
    boolean noticePublish(Notice notice);

    List<NoticeVo> searchNotices(Notice notice);

    List<NoticeVo> searchNoticeById(Long noticeId);

    boolean editNotice(Notice notice);

    boolean deleteNotice(Notice notice);

    List<NoticeVo> searchUserNotices(NoticeVo noticeVo);
}
