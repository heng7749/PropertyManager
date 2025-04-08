package com.heng.property_manager.service;

import com.heng.property_manager.pojo.entity.Menu;
import com.heng.property_manager.pojo.entity.Role;

import java.util.List;

public interface RoleService {
    boolean addRole(Role role);

    boolean editRole(Role role);

    boolean deleteRole(Role role);

    boolean bindUsersToRole(Long roleId, List<Long> usersId);

    boolean unbindRole(Long id);

    List<Menu> getMenus();
}
