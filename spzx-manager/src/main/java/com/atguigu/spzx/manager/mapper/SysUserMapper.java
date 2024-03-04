package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.system.SysUser;
import jdk.jfr.Name;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper {

    SysUser selectByUserName(String userName);
}
