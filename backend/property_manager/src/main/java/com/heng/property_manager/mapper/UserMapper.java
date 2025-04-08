package com.heng.property_manager.mapper;

import com.heng.property_manager.pojo.entity.Menu;
import com.heng.property_manager.pojo.entity.Role;
import com.heng.property_manager.pojo.entity.User;
import com.heng.property_manager.pojo.vo.RoleVo;
import com.heng.property_manager.pojo.vo.UserVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from sys_user where name = #{userName} and is_deleted=0 limit 1")
    User getUserByName(String userName);

    List<String> selectPermsByUserId(Long userId);

    List<Menu> selectUserMenuByUserId(Long userId);
    @Select("select bind_id from sys_user where is_deleted = 0 and type = 1 and id = #{id}")
    Long searchEmployeeIdByUserId(Long id);

    List<UserVo> listUsers(UserVo userVo);

    List<Role> searchRoles(Role role);

    List<UserVo> searchRoleUsers(RoleVo roleVo);
    @Select("select name from owner where id = #{bindId} limit 1")
    String getOwnerNameById(Long bindId);
    @Select("select name from employee where id = #{bindId} limit 1")
    String getEmployeeNameById(Long bindId);

    @Select("select department_id from employee where id=#{bindId} limit 1")
    Long searchDepartmentByEmployeeId(Long bindId);

    @Insert("insert into sys_user(name,password,type,bind_id,create_time,create_by) " +
            "values(#{name},#{password},#{type},#{bindId},#{createTime},#{createBy})")
    boolean addUser(User user);

    boolean editUser(User user);

    @Update("update sys_user set is_deleted = 1 where id = #{id}")
    boolean deleteUser(User user);

    @Select("select * from owner where id = #{bindId} limit 1")
    Object searchOwnerMsgById(Long bindId);

    @Select("select * from employee where id = #{bindId} limit 1")
    Object searchEmployeeMsgById(Long bindId);
}
