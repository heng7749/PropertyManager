package com.heng.property_manager.service;

import com.heng.property_manager.pojo.entity.Advise;
import com.heng.property_manager.pojo.vo.AdviseVo;

import java.util.List;

public interface AdviseService {
    List<AdviseVo> searchAdvice(Advise advise);

    List<AdviseVo> myAdvice(Advise advise);

    boolean addAdvice(Advise advise);

    boolean editAdvice(Advise advise);

    boolean conductAdvice(Advise advise);

    boolean deleteAdvice(Advise advise);
}
