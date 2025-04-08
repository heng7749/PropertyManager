package com.heng.property_manager.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.heng.property_manager.pojo.entity.LoginUser;
import com.heng.property_manager.utils.JwtHmacUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = httpServletRequest.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        //解析token
        String userId;
        try {
            Claims claims = JwtHmacUtil.parseHmacJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("非法token");
        }
        //从Redis中获取用户信息
        String redisKey = "login:" + userId;
        JSONObject object = (JSONObject) redisTemplate.opsForValue().get(redisKey);
        LoginUser loginUser = JSON.toJavaObject(object, LoginUser.class);
        if (Objects.isNull(loginUser)) {
            throw new RuntimeException("用户未登录");
        }

        //存入SecurityContextHolder中
        //获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
