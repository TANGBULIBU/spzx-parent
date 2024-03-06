package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.SysRoleMapper;
import com.atguigu.spzx.manager.service.SysRoleService;
import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Override
    public PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, Integer pageNum, Integer pageSize) {
        //分页传值
        PageHelper.startPage(pageNum, pageSize);

        //通过分页查询找到相关数据 用list集合返回
        List<SysRole> sysRoles = sysRoleMapper.findByPage(sysRoleDto);
        PageInfo<SysRole> pageInfo = new PageInfo(sysRoles);

        return pageInfo;
    }

    //新增角色 也就是保存用户信息
    @Override
    public void saveSysRole(SysRole sysRole) {
        sysRoleMapper.saveSysRole(sysRole);

    }

    //提交修改后的信息

    @Override
    public void updateSysRole(SysRole sysRole) {
        sysRoleMapper.updateSysRole(sysRole);
    }


    //删除角色
    @Override
    public void deleteById(Long roleId) {
        sysRoleMapper.deleteById(roleId);

    }


    @Override
    public Map<String, Object> findAllRoles(Long userId) {
        //使用List接收
        List<SysRole> sysRolesList =
                sysRoleMapper.findAllRoles();

        List<Long> sysRoles=sysRoleMapper.findSysUserRoleByUserId(userId);

        Map<String ,Object> map=new HashMap<>();
        //将list插入map中
        map.put("allRolesList",sysRolesList);
        map.put("sysUserRoles",sysRoles);
        return map;
    }


}
