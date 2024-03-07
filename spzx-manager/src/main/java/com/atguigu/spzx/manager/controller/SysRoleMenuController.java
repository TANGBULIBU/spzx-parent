package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.SysRoleMenuService;
import com.atguigu.spzx.model.dto.system.AssginMenuDto;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "分配菜单")
@RestController
@RequestMapping("/admin/system/sysRoleMenu")
public class SysRoleMenuController {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Operation(summary = "查询菜单")
    @GetMapping("/findSysRoleMenuByRoleId/{roleId}")
    public Result<Map<String ,Object>> findSysRoleMenuByRoleId(@PathVariable(value = "roleId") Long roleId){
        Map<String ,Object> map= sysRoleMenuService.findSysRoleMenuByRoleId(roleId);
        return  Result.build(map, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "保存勾选菜单")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginMenuDto assginMenuDto){
        sysRoleMenuService.doAssign(assginMenuDto);
        return  Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
