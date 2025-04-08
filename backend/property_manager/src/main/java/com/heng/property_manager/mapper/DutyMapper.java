package com.heng.property_manager.mapper;

import com.heng.property_manager.pojo.entity.Duty;
import com.heng.property_manager.pojo.vo.DutyVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DutyMapper {
    List<DutyVo> searchDuty(DutyVo dutyVo);

    @Insert("insert into duty(employee_id,shift,job,attended_date,create_by) " +
            "values(#{employeeId},#{shift},#{job},#{attendedDate},#{createBy})")
    boolean addDuty(Duty duty);

    @Update("update duty set is_deleted = 1 where id = #{id}")
    boolean deleteDuty(Long id);

    List<DutyVo> searchDutyByAttendedDate(DutyVo dutyVo);
}
