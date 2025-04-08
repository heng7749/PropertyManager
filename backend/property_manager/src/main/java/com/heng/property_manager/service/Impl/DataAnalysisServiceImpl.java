package com.heng.property_manager.service.Impl;

import com.heng.property_manager.mapper.SystemDataMapper;
import com.heng.property_manager.service.DataAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataAnalysisServiceImpl implements DataAnalysisService {
    @Autowired
    private SystemDataMapper systemDataMapper;

    @Override
    public Map<String, Object> getSystem() {
        HashMap<String, Object> map = new HashMap<>();
        Long systemUserCount = systemDataMapper.systemUserCount();
        map.put("systemUserCount",systemUserCount);
        Long ownerCount = systemDataMapper.ownerCount();
        map.put("ownerCount",ownerCount);
        Long employeeCount = systemDataMapper.employeeCount();
        map.put("employeeCount",employeeCount);
        Long houseCount = systemDataMapper.houseCount();
        map.put("houseCount",houseCount);
        Long ownerMaleCount = systemDataMapper.ownerMaleCount();
        map.put("ownerMaleCount",ownerMaleCount);
        Long employeeMaleCount = systemDataMapper.employeeMaleCount();
        map.put("employeeMaleCount",employeeMaleCount);

        List<Map<String, Object>> lastSixRepairCount = systemDataMapper.lastSixRepairCount();
        Map<Object, Object> lastSixRepairMap = lastSixRepairCount.stream().collect(Collectors.toMap(e->e.get("time"),e->e.get("count")));
        map.put("lastSixRepairCount",lastSixRepairMap);

        List<Map<String, Object>> lastSixComplaintCount = systemDataMapper.lastSixComplaintCount();
        Map<Object, Object> lastSixComplaintMap = lastSixComplaintCount.stream().collect(Collectors.toMap(e->e.get("time"),e->e.get("count")));
            map.put("lastSixComplaintCount",lastSixComplaintMap);

        List<Map<String, Object>> lastFourNoticeCount = systemDataMapper.lastFourNoticeCount();
        Map<Object, Object> lastFourNoticeMap = lastFourNoticeCount.stream().collect(Collectors.toMap(e->e.get("time"),e->e.get("count")));
        map.put("lastFourNoticeCount",lastFourNoticeMap);

        return map;
    }
}
