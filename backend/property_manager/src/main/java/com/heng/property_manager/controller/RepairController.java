package com.heng.property_manager.controller;

import cn.hutool.core.lang.generator.UUIDGenerator;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heng.property_manager.pojo.entity.Repair;
import com.heng.property_manager.pojo.entity.Stall;
import com.heng.property_manager.pojo.response.ResponseCode;
import com.heng.property_manager.pojo.response.ResponseResult;
import com.heng.property_manager.pojo.vo.RepairVo;
import com.heng.property_manager.pojo.vo.StallVo;
import com.heng.property_manager.service.ParkingService;
import com.heng.property_manager.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("repair")
public class RepairController {
    private static final int PAGE_SIZE = 8;

    @Autowired
    private RepairService repairService;

    @PostMapping("searchRepairs")
    public ResponseResult searchRepairs(@RequestBody RepairVo repairVo){
        Page<RepairVo> page = PageHelper.startPage((int)repairVo.getParams().get("currentPage"), PAGE_SIZE);
        repairService.searchRepairs(repairVo);
        PageInfo<RepairVo> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("myRepairRecords")
    public ResponseResult myRepairRecords(@RequestBody RepairVo repair){
        Page<RepairVo> page = PageHelper.startPage((int)repair.getParams().get("currentPage"), PAGE_SIZE);
        repairService.listUserRepairRecords(repair);
        PageInfo<RepairVo> pageInfo = page.toPageInfo();
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"成功获取",pageInfo);
    }

    @PostMapping("addRepair")
    public ResponseResult addRepair(@RequestParam("photo") MultipartFile file, @RequestParam("houseId") Long houseId,
                                    @RequestParam("type") int type, @RequestParam("phoneNumber") String phoneNumber,@RequestParam("contents") String contents){
        Repair repair = new Repair();
        repair.setHouseId(houseId);
        repair.setType(type);
        repair.setPhoneNumber(phoneNumber);
        repair.setContents(contents);
        //图片地址
        String path= ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static\\repairImg\\";
        String filename = file.getOriginalFilename();
        String prefix = "http://localhost:8000/";
        String suffix = filename.substring(filename.lastIndexOf("."));;
        String photoName = UUID.randomUUID() + suffix;
        try {
            file.transferTo(new File(path + photoName));
            String photoPath = prefix + "repairImg/" + photoName;
            repair.setPhoto(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean is_succeed =repairService.addRepair(repair);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"添加成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败",null);
        }
    }

    @PostMapping("editRepair")
    public ResponseResult editRepair(@RequestParam(value = "photo",required = false) MultipartFile file, @RequestParam("id") Long id,
                                    @RequestParam("type") int type, @RequestParam("phoneNumber") String phoneNumber,@RequestParam("contents") String contents){
        Repair repair = new Repair();
        repair.setId(id);
        repair.setType(type);
        repair.setPhoneNumber(phoneNumber);
        repair.setContents(contents);
        if (file != null) {
            try {
                //图片地址
                File path = new File(ResourceUtils.getURL("classpath:").getPath());
                String filename = file.getOriginalFilename();
                String prefix = "http://localhost:8000/";
                String suffix = filename.substring(filename.lastIndexOf("."));;
                String photoName = UUID.randomUUID() + suffix;
                file.transferTo(new File(path.getAbsolutePath(),"static/repairImg/" + photoName));
                String photoPath = prefix + "repairImg/" + photoName;
                repair.setPhoto(photoPath);
           } catch (IOException e) {
                e.printStackTrace();
            }
        }
        boolean is_succeed =repairService.editRepair(repair);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"添加成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"添加失败",null);
        }
    }

    @PostMapping("deleteRepair")
    public ResponseResult deleteRepair(@RequestBody Repair repair){
        boolean is_succeed =repairService.deleteRepair(repair.getId());
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"删除成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"删除失败",null);
        }
    }

    @PostMapping("conductRepair")
    public ResponseResult conductRepair(@RequestBody Repair repair){
        boolean is_succeed =repairService.conductRepair(repair);
        if (is_succeed) {
            return new ResponseResult<>(ResponseCode.SUCCESS.getCode(),"处理成功",null);
        } else {
            return new ResponseResult<>(ResponseCode.ERROR.getCode(),"处理失败",null);
        }
    }
}
