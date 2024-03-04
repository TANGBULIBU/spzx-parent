package com.atguigu.spzx.manager.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.atguigu.spzx.common.exception.GuiguException;
import com.atguigu.spzx.manager.mapper.SysUserMapper;
import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public LoginVo login(LoginDto loginDto) {

        //添加校验验证码 是否与用户输入一致
        String captcha = loginDto.getCaptcha();
        String codeKey = loginDto.getCodeKey();

        //从Redis中获取验证码
        String redisCode = redisTemplate.opsForValue().get("user:login:validatecode:" + codeKey);
        if (StrUtil.isEmpty(redisCode) || !StrUtil.equalsIgnoreCase(redisCode, captcha)) {
            throw new GuiguException(ResultCodeEnum.VALIDATECODE_ERROR);
        }
        redisTemplate.delete("user:login:validatecode:" + codeKey);

        //检验用户名是否存在
        SysUser sysUser = sysUserMapper.selectByUserName(loginDto.getUserName());
        if (sysUser == null) {
            throw new GuiguException(ResultCodeEnum.LOGIN_USERNAME_ERROR);
//            throw new RuntimeException("用户名或者密码错误");
        }

        //校验密码
        String inputPassword = loginDto.getPassword();//输入的密码
        String password = sysUser.getPassword();
        String md5password = DigestUtils.md5DigestAsHex(inputPassword.getBytes());
        if (!password.equals(md5password)) {
            throw new GuiguException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);

//            throw new RuntimeException("用户名或者密码错误");
        }

        // 使用uuid 添加令牌
        String token = UUID.randomUUID().toString().replace("-", "");

        //使用redis存储token令牌
        redisTemplate.opsForValue().set("user:login:" + token, JSON.toJSONString(sysUser), 30, TimeUnit.MINUTES);

        //组装返回结果
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        loginVo.setRefresh_token("");//刷新令牌
        return loginVo;
    }

    //根据token获取用户数据接口的方法

    @Override
    public SysUser getUserInfo(String token) {
        String userJson = redisTemplate.opsForValue().get("user:login:" + token);
        //返回json数据
        return JSON.parseObject(userJson, SysUser.class);
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete("user:login:" + token);
    }
}
