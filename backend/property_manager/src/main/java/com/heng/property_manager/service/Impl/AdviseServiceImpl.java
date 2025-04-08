package com.heng.property_manager.service.Impl;

import com.heng.property_manager.mapper.AdviseMapper;
import com.heng.property_manager.pojo.entity.Advise;
import com.heng.property_manager.pojo.entity.LoginUser;
import com.heng.property_manager.pojo.vo.AdviseVo;
import com.heng.property_manager.service.AdviseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AdviseServiceImpl implements AdviseService {
    @Autowired
    private AdviseMapper adviseMapper;

    @Override
    public List<AdviseVo> searchAdvice(Advise advise) {
        return adviseMapper.searchAdvice(advise);
    }

    @Override
    public List<AdviseVo> myAdvice(Advise advise) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        advise.setOwnerId(principal.getUser().getId());
        return adviseMapper.listUserAdvice(advise);
    }

    @Override
    public boolean addAdvice(Advise advise) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 获取业主id
        advise.setOwnerId(principal.getUser().getBindId());
        advise.setCreateTime(new Date());
        return adviseMapper.addAdvice(advise);
    }

    @Override
    public boolean editAdvice(Advise advise) {
        return adviseMapper.editAdvice(advise);
    }

    @Override
    public boolean conductAdvice(Advise advise) {
        if (advise.getStatus() == 1) {
            LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // 获取操作者id
            advise.setUpdateBy(principal.getUser().getBindId());
            advise.setUpdateTime(new Date());
        } else {
            advise.setFeedback("");
            advise.setUpdateTime(null);
        }
        return adviseMapper.conductAdvice(advise);
    }

    @Override
    public boolean deleteAdvice(Advise advise) {
        return adviseMapper.deleteAdvice(advise);
    }
}
