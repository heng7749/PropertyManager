package com.heng.property_manager.service.Impl;

import com.heng.property_manager.mapper.UserMapper;
import com.heng.property_manager.pojo.entity.*;
import com.heng.property_manager.pojo.response.ResponseCode;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.pojo.vo.EmployeeVo;
import com.heng.property_manager.pojo.vo.RoleVo;
import com.heng.property_manager.pojo.vo.UserVo;
import com.heng.property_manager.service.UserService;
import com.heng.property_manager.utils.JwtHmacUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult login(User user) {
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //认证失败
        if (Objects.isNull(authenticate))
            throw new RuntimeException("登录失败");
        //认证成功
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String hmacJWT = JwtHmacUtil.createHmacJWT(null,userId,30 * 60 * 1000L);
        //用户信息存入Redis中
        Map<String,String> map = new HashMap<>();
        map.put("token",hmacJWT);
        redisTemplate.opsForValue().set("login:" + userId, loginUser);
        return new ResponseResult(ResponseCode.SUCCESS.getCode(), "登录成功", map);
    }

    @Override
    public List<Menu> getMenuByUserId(Long id) {
        List<Menu> menus = userMapper.selectUserMenuByUserId(id);
        return menus;
    }

    @Override
    public List<UserVo> searchUsers(UserVo userVo) {
        return userMapper.listUsers(userVo);
    }

    @Override
    public List<Role> searchRoles(Role role) {
        return userMapper.searchRoles(role);
    }

    @Override
    public List<UserVo> searchRoleUsers(RoleVo roleVo) {
        return userMapper.searchRoleUsers(roleVo);
    }

    @Override
    public boolean addUser(User user) {
        LoginUser principal = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setCreateBy(principal.getUser().getBindId());
        user.setCreateTime(new Date());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.addUser(user);
    }

    @Override
    public boolean editUser(User user) {
        if (user.getPassword() != null && user.getPassword().length() > 0) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userMapper.editUser(user);
    }

    @Override
    public boolean deleteUser(User user) {
        return userMapper.deleteUser(user);
    }

    @Override
    public Object searchUserBindMsg(User user) {
        if (user.getType() == 0) {
            return userMapper.searchOwnerMsgById(user.getBindId());
        } else {
            return userMapper.searchEmployeeMsgById(user.getBindId());
        }
    }

    @Override
    public String getOwnerNameById(Long bindId) {
        return userMapper.getOwnerNameById(bindId);
    }

    @Override
    public String getEmployeeNameById(Long bindId) {
        return userMapper.getEmployeeNameById(bindId);
    }
}
