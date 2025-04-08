package com.heng.property_manager.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heng.property_manager.pojo.entity.*;
import com.heng.property_manager.pojo.response.ResponseCode;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.pojo.vo.EmployeeVo;
import com.heng.property_manager.pojo.vo.PurchaseVo;
import com.heng.property_manager.pojo.vo.RoleVo;
import com.heng.property_manager.pojo.vo.UserVo;
import com.heng.property_manager.service.UserService;
import kotlin.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Templates;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    private static final int PAGE_SIZE = 8;

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/login")
    public ResponseResult userLogin(@RequestBody User user) {
        return userService.login(user);
    }

    @GetMapping("/logout")
    public ResponseResult logout() {
        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        //删除Redis中的值
        redisTemplate.delete("login:" + userId);
        return new ResponseResult(ResponseCode.SUCCESS.getCode(),"注销成功");
    }

    @GetMapping("/menu")
    public ResponseResult loadMenuTree() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Menu> menus = userService.getMenuByUserId(loginUser.getUser().getId());
        menus.sort(Comparator.comparing(Menu::getOrderNum));
        List<Menu> result = new ArrayList<>();
        for (Menu menu : menus) {
            if (menu.getParentId() == null) {
                List<Menu> children = new ArrayList<>();
                Long menuId = menu.getId();
                for (Menu item : menus) {
                    if (item.getParentId() == menuId) {
                        children.add(item);
                    }
                }
                menu.setChildren(children);
                result.add(menu);
            }
        }
        return new ResponseResult(ResponseCode.SUCCESS.getCode(),"获取菜单成功",result);
    }

    @PostMapping("searchUsers")
    public ResponseResult searchUsers(@RequestBody UserVo userVo){
        Page<UserVo> page = PageHelper.startPage((int)userVo.getParams().get("currentPage"), PAGE_SIZE);
        userService.searchUsers(userVo);
        PageInfo<UserVo> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("searchRoles")
    public ResponseResult searchRoles(@RequestBody Role role){
        Page<Role> page = PageHelper.startPage((int)role.getParams().get("currentPage"), PAGE_SIZE);
        userService.searchRoles(role);
        PageInfo<Role> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("searchRoleUsers")
    public ResponseResult searchRoleUsers(@RequestBody RoleVo roleVo){
        Page<UserVo> page = PageHelper.startPage((int)roleVo.getParams().get("currentPage"), PAGE_SIZE);
        userService.searchRoleUsers(roleVo);
        PageInfo<UserVo> pageInfo = page.toPageInfo();

        for (UserVo userVo : pageInfo.getList()) {
            if (userVo.getType() == 0) {
                Owner owner = new Owner();
                owner.setName(userService.getOwnerNameById(userVo.getBindId()));
                userVo.setOwner(owner);
            } else {
                EmployeeVo employeeVo = new EmployeeVo();
                employeeVo.setName(userService.getEmployeeNameById(userVo.getBindId()));
                userVo.setEmployeeVo(employeeVo);
            }
        }
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("addUser")
    public ResponseResult addUser(@RequestBody User user){
        boolean is_succeed =userService.addUser(user);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"添加成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败",null);
        }
    }

    @PostMapping("editUser")
    public ResponseResult editUser(@RequestBody User user){
        boolean is_succeed =userService.editUser(user);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"修改成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败",null);
        }
    }

    @PostMapping("deleteUser")
    public ResponseResult deleteUser(@RequestBody User user){
        boolean is_succeed =userService.deleteUser(user);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"删除失败",null);
        }
    }

    @PostMapping("userBindMsg")
    public ResponseResult searchUserBindMsg(@RequestBody User user){
        Object bindMsg = userService.searchUserBindMsg(user);
        return new ResponseResult(ResponseCode.SUCCESS.getCode(),bindMsg);
    }
}
