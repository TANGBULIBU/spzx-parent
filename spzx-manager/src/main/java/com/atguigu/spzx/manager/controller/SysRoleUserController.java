package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.SysRoleUserService;
import com.atguigu.spzx.model.dto.system.AssginRoleDto;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户角色管理")

@RestController
@RequestMapping("/admin/system/sysRoleUser")
public class SysRoleUserController {

    @Autowired
    private SysRoleUserService sysRoleUserService;

    //修改角色信息的时候需要 提交事务 对意外情况进行回滚
    @Transactional
    @Operation(summary = "角色分配")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleDto assginRoleDto) {
        sysRoleUserService.doAssign(assginRoleDto) ;
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }


}
