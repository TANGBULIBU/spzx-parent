package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.SysRoleMapper;
import com.atguigu.spzx.manager.mapper.SysRoleUserMapper;
import com.atguigu.spzx.manager.service.SysRoleService;
import com.atguigu.spzx.manager.service.SysRoleUserService;
import com.atguigu.spzx.model.dto.system.AssginRoleDto;
import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleUserServiceImpl implements SysRoleUserService {


    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Override
    public void doAssign(AssginRoleDto assginRoleDto) {

        //如果确定更改 就需要更改用户之前所有的角色数据 重新对部分数据进行分配
        sysRoleUserMapper.deleteByUserId(assginRoleDto.getUserId());

        //添加现有的数据
        List<Long> roleIdList = assginRoleDto.getRoleIdList();
        roleIdList.forEach(roleId -> {
            sysRoleUserMapper.doAssign(assginRoleDto.getUserId(), roleId);

        });

    }
}
