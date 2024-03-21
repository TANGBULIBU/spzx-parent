package com.atguigu.spzx.user.controller;

import com.atguigu.spzx.model.dto.h5.UserLoginDto;
import com.atguigu.spzx.model.dto.h5.UserRegisterDto;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.h5.UserInfoVo;
import com.atguigu.spzx.user.service.UserInfoService;
import com.atguigu.spzx.utils.IpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "会员用户接口")
@RestController
@RequestMapping("/api/user/userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Operation(summary = "会员注册")
    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterDto userRegisterDto) {
        userInfoService.register(userRegisterDto);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "会员登录")
    @PostMapping("/login")
    // @RequestBody 会自动将请求体转为对象
    public Result login(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request) {
        String ipAddr = IpUtil.getIpAddr(request);//获取远程客户端得地址
        String token = userInfoService.login(userLoginDto, ipAddr);
        return Result.build(token, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "获取当前用户登录的信息")
    @GetMapping("/auth/getCurrentUserInfo")
    // @RequestBody 会自动将请求体转为对象
    public Result getCurrentUserInfo(HttpServletRequest request) {
        //获取登录时得token
        String token = request.getHeader("token");

        UserInfoVo userInfoVo = userInfoService.getCurrentUserInfo(token);
        return Result.build(userInfoVo, ResultCodeEnum.SUCCESS);
    }

}