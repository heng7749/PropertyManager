package com.heng.property_manager.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface SystemDataMapper {
    @Select("select count(1) from sys_user where is_deleted = 0")
    Long systemUserCount();

    @Select("select count(1) from owner where is_deleted = 0")
    Long ownerCount();

    @Select("select count(1) from employee where is_deleted = 0")
    Long employeeCount();

    @Select("select count(1) from house where is_deleted = 0")
    Long houseCount();

    @Select("select count(1) from owner where is_deleted = 0 AND gender = 1 ")
    Long ownerMaleCount();

    @Select("select count(1) from employee where is_deleted = 0 AND gender = 1 ")
    Long employeeMaleCount();

    @Select("SELECT * FROM\n" +
            "(SELECT DATE_FORMAT(create_time,'%Y-%m') time,COUNT(1) count FROM repair GROUP BY time) d\n" +
            "WHERE d.time BETWEEN DATE_FORMAT(CURDATE() - INTERVAL 5 MONTH,'%Y-%m') AND DATE_FORMAT(CURDATE(),'%Y-%m')")
    List<Map<String,Object>> lastSixRepairCount();

    @Select("SELECT d.time,d.count FROM\n" +
            "(SELECT DATE_FORMAT(create_time,'%Y-%m') time,COUNT(1) count FROM `complaint` GROUP BY time) d\n" +
            "WHERE d.time BETWEEN DATE_FORMAT(CURDATE() - INTERVAL 5 MONTH,'%Y-%m') AND DATE_FORMAT(CURDATE(),'%Y-%m')")
    List<Map<String,Object>> lastSixComplaintCount();

    @Select("SELECT d.time,d.count FROM\n" +
            "(SELECT DATE_FORMAT(create_time,'%Y-%m') time,COUNT(1) count FROM `notice` GROUP BY time) d\n" +
            "WHERE d.time BETWEEN DATE_FORMAT(CURDATE() - INTERVAL 3 MONTH,'%Y-%m') AND DATE_FORMAT(CURDATE(),'%Y-%m')")
    List<Map<String,Object>> lastFourNoticeCount();
}
