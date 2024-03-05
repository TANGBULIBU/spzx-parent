package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.SysRoleService;
import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/system/sysRole")

public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

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
    @PostMapping("/saveSysRole")
    public Result saveSysRole(@RequestBody SysRole sysRole) {
        sysRoleService.saveSysRole(sysRole);
        return Result.build(null, ResultCodeEnum.SUCCESS);

    }

    //提交修改后的信息
    @PutMapping("/updateSysRole")
    public Result updateSysRole(@RequestBody SysRole sysRole) {
        sysRoleService.updateSysRole(sysRole);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //删除角色
    @DeleteMapping("/deleteById/{roleId}")
    public Result deleteById(@PathVariable("roleId") Long roleId ) {
        sysRoleService.deleteById(roleId);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //查询所有的角色 例如：管理员等
    @GetMapping("/findAllRoles")
    public Result<Map<String ,Object>> findAllRoles(){
        Map<String ,Object> map= sysRoleService.findAllRoles();
        return Result.build(map, ResultCodeEnum.SUCCESS);
    }

}
