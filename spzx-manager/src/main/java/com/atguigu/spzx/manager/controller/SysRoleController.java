package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.common.log.annotation.Log;
import com.atguigu.spzx.common.log.enums.OperatorType;
import com.atguigu.spzx.manager.service.SysRoleService;
import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "角色管理")
@RestController
@RequestMapping("/admin/system/sysRole")

public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @Log(title = "添加角色",businessType = 0)
    @Operation(summary = "查找分页数据")
    @PostMapping("/findByPage/{pageNum}/{pageSize}")
    //一个请求体 两个路径传参
    public Result<PageInfo<SysRole>> findByPage(@RequestBody SysRoleDto sysRoleDto,
                                                @PathVariable(value = "pageNum") Integer pageNum,
                                                @PathVariable(value = "pageSize") Integer pageSize) {

        //传入实体类 页数和页大小
        PageInfo<SysRole> pageInfo = sysRoleService.findByPage(sysRoleDto, pageNum, pageSize);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }


    //新增角色 也就是保存用户信息
    @Operation(summary = "新增角色")
    @PostMapping("/saveSysRole")
    public Result saveSysRole(@RequestBody SysRole sysRole) {
        sysRoleService.saveSysRole(sysRole);
        return Result.build(null, ResultCodeEnum.SUCCESS);

    }

    //提交修改后的信息
    @Operation(summary = "提交修改后角色信息")
    @PutMapping("/updateSysRole")
    public Result updateSysRole(@RequestBody SysRole sysRole) {
        sysRoleService.updateSysRole(sysRole);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //删除角色
    @Operation(summary = "删除角色")
    @DeleteMapping("/deleteById/{roleId}")
    public Result deleteById(@PathVariable("roleId") Long roleId ) {
        sysRoleService.deleteById(roleId);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //查询所有的角色 例如：管理员等
    @Operation(summary = "查询所有角色")
    @GetMapping("/findAllRoles/{userId}")
    public Result<Map<String ,Object>> findAllRoles(@PathVariable(value = "userId")Long userId){

//        Map<String ,Object> map= sysRoleService.findAllRoles();
        Map<String ,Object> map= sysRoleService.findAllRoles(userId);
        return Result.build(map, ResultCodeEnum.SUCCESS);
    }



}
