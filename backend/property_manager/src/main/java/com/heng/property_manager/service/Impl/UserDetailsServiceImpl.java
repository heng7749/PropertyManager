package com.heng.property_manager.service.Impl;

import com.heng.property_manager.mapper.UserMapper;
import com.heng.property_manager.pojo.entity.LoginUser;
import com.heng.property_manager.pojo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        User user = userMapper.getUserByName(username);
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //查询对应的权限信息
        List<String> perms = userMapper.selectPermsByUserId(user.getId());
        return new LoginUser(user, perms);
    }
}
