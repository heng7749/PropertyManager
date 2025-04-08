package com.heng.property_manager.service.Impl;

import com.heng.property_manager.mapper.NoticeMapper;
import com.heng.property_manager.mapper.UserMapper;
import com.heng.property_manager.pojo.entity.LoginUser;
import com.heng.property_manager.pojo.entity.Notice;
import com.heng.property_manager.pojo.vo.NoticeVo;
import com.heng.property_manager.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean noticePublish(Notice notice) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        notice.setCreateBy(principal.getUser().getBindId());
        return noticeMapper.publish(notice);
    }

    @Override
    public List<NoticeVo> searchUserNotices(NoticeVo noticeVo) {
        return noticeMapper.searchUserNotices(noticeVo);
    }

    @Override
    public List<NoticeVo> searchNotices(Notice notice) {
        return noticeMapper.searchNotices(notice);
    }

    @Override
    public List<NoticeVo> searchNoticeById(Long noticeId) {
        return noticeMapper.findNoticeById(noticeId);
    }

    @Override
    public boolean editNotice(Notice notice) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        notice.setUpdateBy(principal.getUser().getBindId());
        notice.setUpdateTime(new Date());
        return noticeMapper.editNotice(notice);
    }

    @Override
    public boolean deleteNotice(Notice notice) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        notice.setUpdateBy(principal.getUser().getBindId());
        notice.setUpdateTime(new Date());
        return noticeMapper.deleteNotice(notice);
    }
}
