package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.common.exception.GuiguException;
import com.atguigu.spzx.manager.helper.MenuHelper;
import com.atguigu.spzx.manager.mapper.SysMenuMapper;
import com.atguigu.spzx.manager.service.SysMenuService;
import com.atguigu.spzx.model.entity.system.SysMenu;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> findNodes() {

        //需要使用MenuHelper中的方法
        //首先使用mapper查询到所有的数据
        List<SysMenu> sysMenusList = sysMenuMapper.selectAll();
        //如果是空就返回
        if (CollectionUtils.isEmpty(sysMenusList)) {
            return null;
        }
//构建树形结构
        List<SysMenu> sysMenus = MenuHelper.buildTree(sysMenusList);
        return sysMenus;
    }

    @Override
    public void save(SysMenu sysMenu) {
        sysMenuMapper.save(sysMenu);
    }

    @Override
    public void updateById(SysMenu sysMenu) {
        sysMenuMapper.updateById(sysMenu);
    }

    @Override
    public void removeById(Long id) {
        //删除操作 前先查询是否该菜单是否存在子菜单
        int count= sysMenuMapper.countByParentId(id);

        //判断是否存在子菜单 就报错 也就是不执行后续的删除操作
        if(count>0){
            throw new GuiguException(ResultCodeEnum.NODE_ERROR);
        }
        //
        sysMenuMapper.removeById(id);
    }
}
