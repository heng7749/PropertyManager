package com.heng.property_manager.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heng.property_manager.pojo.entity.Menu;
import com.heng.property_manager.pojo.entity.Role;
import com.heng.property_manager.pojo.entity.User;
import com.heng.property_manager.pojo.response.ResponseCode;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.pojo.vo.BillTypeVo;
import com.heng.property_manager.service.BillTypeService;
import com.heng.property_manager.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("role")
public class RoleController {
    private static final int PAGE_SIZE = 8;

    @Autowired
    private RoleService roleService;


    @PostMapping("addRole")
    public ResponseResult addRole(@RequestBody Role role){
        boolean is_succeed;
        try {
            is_succeed = roleService.addRole(role);
        } catch (Exception exception) {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败,收费项已存在",null);
        }
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"添加成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败",null);
        }
    }

    @PostMapping("editRole")
    public ResponseResult editRole(@RequestBody Role role){
        boolean is_succeed;
        try {
            is_succeed = roleService.editRole(role);
        } catch (Exception exception) {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败,收费项已存在",null);
        }
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"修改成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"修改失败",null);
        }
    }

    @PostMapping("deleteRole")
    public ResponseResult deleteRole(@RequestBody Role role){
        boolean is_succeed =roleService.deleteRole(role);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"删除失败",null);
        }
    }

    @PostMapping("bindUsersToRole")
    public ResponseResult bindUsersToRole(@RequestParam("roleId") Long roleId,@RequestParam("usersId") List<Long> usersId){
        boolean is_succeed = roleService.bindUsersToRole(roleId,usersId);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"操作成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"操作失败",null);
        }
    }

    @PostMapping("unbindRole")
    public ResponseResult unbindRole(@RequestParam("id") Long id){
        boolean is_succeed =roleService.unbindRole(id);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"操作成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"操作失败",null);
        }
    }

    @GetMapping("permsList")
    public ResponseResult getMenus(){
        List<Menu> menus = roleService.getMenus();
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
        return new ResponseResult(ResponseCode.SUCCESS.getCode(),"获取成功",result);
    }
}
