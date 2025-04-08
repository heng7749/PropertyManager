package com.heng.property_manager;

import cn.hutool.core.date.CalendarUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.heng.property_manager.mapper.UserMapper;
import com.heng.property_manager.pojo.entity.LoginUser;
import com.heng.property_manager.pojo.entity.User;
import com.heng.property_manager.pojo.vo.DutyVo;
import com.heng.property_manager.service.DutyService;
import com.heng.property_manager.utils.JwtHmacUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootTest
class PropertyManagerApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DutyService dutyService;

    @Test
    void contextLoads() {
    }

    @Test
    void generateJwtToken() {
        String hmacJWT = JwtHmacUtil.createHmacJWT("15",null,null);
        Claims claims = JwtHmacUtil.parseHmacJWT(hmacJWT);
        System.out.println(claims.getId());
    }

    @Test
    void test() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String str1 = passwordEncoder.encode("123456");
        String str2 = passwordEncoder.encode("123456");

//        System.out.println(str1);
//        System.out.println(str2);
        System.out.println(passwordEncoder.matches("123456", "        System.out.println(str1);\n"));
        System.out.println(passwordEncoder.matches("123456", "$2a$10$7hkjwvvmEopnOYnv8GndIe/FoD4aJrf.t2//e4X7cnITz9ecnhh/S"));
    }

    @Test
    void test1() {
        List<String> list1 = userMapper.selectPermsByUserId(1L);
        for (String s : list1) {
            System.out.print(s + " ");
        }
        System.out.println();
        List<String> list2 = userMapper.selectPermsByUserId(2L);
        for (String s : list2) {
            System.out.print(s + " ");
        }
        System.out.println();
    }

    @Test
    void test2() {
        User user = userMapper.getUserByName("admin");
        List<String> list1 = userMapper.selectPermsByUserId(1L);
        LoginUser loginUser = new LoginUser(user, list1);
        Collection<? extends GrantedAuthority> authorities = loginUser.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            System.out.println(authority);
        }
    }

    @Test
    void test3() {
        JSONObject object = (JSONObject)redisTemplate.opsForValue().get("login:1");
        LoginUser loginUser = JSON.toJavaObject(object, LoginUser.class);
        System.out.println(loginUser);
    }

    @Test
    void test4() {
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,7*4);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));

    }

    @Test
    void test5() {
        DutyVo dutyVo = new DutyVo();
        dutyVo.setDutyMonth("2023-03-01");
        List<DutyVo> result =dutyService.searchDuty(dutyVo);
        Map<String, List<DutyVo>> collect = result.stream().collect(Collectors.groupingBy(DutyVo::getAttendedDate));
        collect.forEach((k,v)->{
            System.out.println(k+" -----> "+v);
        });
    }

    @Test
    void test6(){
        Date startTime = new Date(2023,2,28,11,0,0);
        Date endTime = new Date(2023,2,28,13,29,0);
        long between = DateUtil.between(startTime, endTime, DateUnit.MINUTE);
        long diff = between / 60;
        if (between%60 >= 30) {
            diff++;
        }
        System.out.println(diff);
    }
}
