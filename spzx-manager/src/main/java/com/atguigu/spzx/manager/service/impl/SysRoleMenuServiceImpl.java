package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.SysRoleMenuMapper;
import com.atguigu.spzx.manager.service.SysMenuService;
import com.atguigu.spzx.manager.service.SysRoleMenuService;
import com.atguigu.spzx.model.dto.system.AssginMenuDto;
import com.atguigu.spzx.model.entity.system.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleMenuServiceImpl implements SysRoleMenuService {
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    //使用他的方法查询所有的菜单数据
    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public Map<String, Object> findSysRoleMenuByRoleId(Long roleId) {

        // 查询所有的菜单数据
        List<SysMenu> sysMenuList = sysMenuService.findNodes();

        System.out.println(sysMenuList);

        //查询当前角色的菜单数据
        List<Long> roleMenuIds = sysRoleMenuMapper.findSysRoleMenuByRoleId(roleId);

        //将他们的数据用于map接收和返回
        HashMap<String, Object> map = new HashMap<>();

        map.put("sysMenuList", sysMenuList);
        map.put("roleMenuIds", roleMenuIds);

        return map;
    }

    @Override
    public void doAssign(AssginMenuDto assginMenuDto) {
        //要对勾选的菜单进行保存 还需要对其内容 先进行删除操作 再保存其相关数据

        //传入的值是获得到的id值
        sysRoleMenuMapper.deleteByRoleId(assginMenuDto.getRoleId());

        //选中的菜单的id
        //mapper中的sql方法 foreach循环插入数据
        List<Map<String, Number>> menuIdList = assginMenuDto.getMenuIdList();
        System.out.println(menuIdList);

        //选中的内容是否存在菜单
        if (menuIdList != null && menuIdList.size() > 0) {
            sysRoleMenuMapper.doAssign(assginMenuDto);
        }
    }
}
