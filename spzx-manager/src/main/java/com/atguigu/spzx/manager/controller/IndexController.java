package com.atguigu.spzx.manager.controller;


import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.manager.service.ValidateCodeService;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;
import com.atguigu.spzx.model.vo.system.ValidateCodeVo;
import com.atguigu.spzx.utils.AuthContextUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户接口")
@RestController
@RequestMapping("/admin/system/index")

//添加跨域请求 并且和前端
//@CrossOrigin(allowCredentials = "true",originPatterns = "*",allowedHeaders = "token")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ValidateCodeService validateCodeService;

    @Operation(summary = "登录接口")
    @PostMapping("/login")
    public Result<LoginVo> login(@RequestBody LoginDto loginDto) {
        LoginVo login = sysUserService.login(loginDto);

        return Result.build(login, ResultCodeEnum.SUCCESS);

    }

    //获取验证码接口
    @Operation(summary = "获取验证码接口")
    @GetMapping("/generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode() {
        ValidateCodeVo vo = validateCodeService.generateValidateCode();
        return Result.build(vo, ResultCodeEnum.SUCCESS);
    }

    //获取用户信息接口
    @Operation(summary = "获取用户信息接口")
    @GetMapping("/getUserInfo")
    //根据token的值获取用户信息
    //获取用户信息接口不需要获取token 从Redis查询
    public Result<SysUser> getUserInfo(){
//        SysUser userInfo = sysUserService.getUserInfo(token);
//        return Result.build(userInfo, ResultCodeEnum.SUCCESS);
        return Result.build(AuthContextUtil.get(),ResultCodeEnum.SUCCESS);
    }


    @Operation(summary = "退出")
    @GetMapping("/logout")
    public Result logout(@RequestHeader(value="token") String token){
        sysUserService.logout(token);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    //添加用户 保存用户信息
    @PostMapping(value = "/saveSysUser")
    public Result saveSysUser(@RequestBody SysUser sysUser) {
        sysUserService.saveSysUser(sysUser) ;
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }
}