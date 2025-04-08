package com.heng.property_manager.service.Impl;

import com.heng.property_manager.mapper.BuildingMapper;
import com.heng.property_manager.mapper.RoleMapper;
import com.heng.property_manager.pojo.entity.LoginUser;
import com.heng.property_manager.pojo.entity.Menu;
import com.heng.property_manager.pojo.entity.Role;
import com.heng.property_manager.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class roleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public boolean addRole(Role role) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        role.setCreateBy(principal.getUser().getBindId());
        role.setCreateTime(new Date());
        return roleMapper.addRole(role);
    }

    @Override
    public boolean editRole(Role role) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        role.setUpdateBy(principal.getUser().getBindId());
        role.setUpdateTime(new Date());
        return roleMapper.editRole(role);
    }

    @Override
    public boolean deleteRole(Role role) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        role.setUpdateBy(principal.getUser().getBindId());
        role.setUpdateTime(new Date());
        return roleMapper.deleteRole(role);
    }

    @Override
    public boolean bindUsersToRole(Long roleId, List<Long> usersId) {
        for (Long userId : usersId) {
            try {
                roleMapper.bindUserToRole(roleId,userId);
            } catch (Exception e){
            }
        }
        return true;
    }

    @Override
    public boolean unbindRole(Long id) {
        return roleMapper.unbindRole(id);
    }

    @Override
    public List<Menu> getMenus() {
        return roleMapper.getMenus();
    }
}
