package com.heng.property_manager.mapper;

import com.heng.property_manager.pojo.entity.Menu;
import com.heng.property_manager.pojo.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleMapper {
    @Insert("insert into sys_role(name,code,remark,create_time,create_by) " +
            "values(#{name},#{code},#{remark},#{createTime},#{createBy})")
    boolean addRole(Role role);

    @Update("update sys_role set name=#{name},code=#{code},remark=#{remark},update_by=#{updateBy},update_time=#{updateTime} where id=#{id}")
    boolean editRole(Role role);

    @Update("update sys_role set is_deleted = 1,update_by=#{updateBy},update_time=#{updateTime} where id = #{id}")
    boolean deleteRole(Role role);

    boolean bindUsersToRole(Long roleId, List<Long> usersId);

    @Insert("insert into sys_user_role(user_id,role_id) values (#{userId},#{roleId})")
    boolean bindUserToRole(Long roleId, Long userId);

    @Delete("delete from sys_user_role where id=#{id}")
    boolean unbindRole(Long id);

    @Select("select * from sys_menu")
    List<Menu> getMenus();
}
