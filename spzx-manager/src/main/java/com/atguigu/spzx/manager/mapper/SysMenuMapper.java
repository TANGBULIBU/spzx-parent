package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysMenuMapper {


    //获取所有菜单的方法
    List<SysMenu> selectAll();

    void save( SysMenu sysMenu);
}
