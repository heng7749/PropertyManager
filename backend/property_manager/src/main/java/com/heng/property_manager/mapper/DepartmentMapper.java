package com.heng.property_manager.mapper;

import com.heng.property_manager.pojo.entity.Department;
import com.heng.property_manager.pojo.vo.DepartmentVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    List<DepartmentVo> searchDepartment(Department department);

    @Insert("insert into department(name,create_time,create_by) " +
            "values(#{name},#{createTime},#{createBy})")
    boolean addDepartment(Department department);

    @Update("update department set name=#{name} where id=#{id}")
    boolean editDepartment(Department department);

    @Update("update department set is_deleted = 1 where id = #{id}")
    boolean deleteDepartment(Department department);
}
