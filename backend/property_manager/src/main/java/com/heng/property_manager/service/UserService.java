package com.heng.property_manager.service;

import com.heng.property_manager.pojo.entity.Menu;
import com.heng.property_manager.pojo.entity.Role;
import com.heng.property_manager.pojo.entity.User;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.pojo.vo.RoleVo;
import com.heng.property_manager.pojo.vo.UserVo;

import java.util.List;

public interface UserService {
    ResponseResult login(User user);

    List<Menu> getMenuByUserId(Long id);

    List<UserVo> searchUsers(UserVo userVo);

    List<Role> searchRoles(Role role);

    List<UserVo> searchRoleUsers(RoleVo roleVo);

    boolean addUser(User user);

    boolean editUser(User user);

    boolean deleteUser(User user);

    Object searchUserBindMsg(User user);

    String getOwnerNameById(Long bindId);

    String getEmployeeNameById(Long bindId);
}
