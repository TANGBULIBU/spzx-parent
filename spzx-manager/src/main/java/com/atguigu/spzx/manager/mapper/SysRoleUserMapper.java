package com.atguigu.spzx.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysRoleUserMapper {

    //将新添加的信息加入数据库
    void doAssign(@Param("userId") Long userId,@Param("roleId") Long roleId);//两个参数分不清 需要对他们进行定义@param

    //删除角色的所有数据 使用新添加的内容
    void deleteByUserId(Long userId);


}
