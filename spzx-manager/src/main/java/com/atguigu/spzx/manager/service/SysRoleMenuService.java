package com.atguigu.spzx.manager.service;

import java.util.Map;

public interface SysRoleMenuService {
    //查询菜单
    Map<String, Object> findSysRoleMenuByRoleId(Long roleId);
}
