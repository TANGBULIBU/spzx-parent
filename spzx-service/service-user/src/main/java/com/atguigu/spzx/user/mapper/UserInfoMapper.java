package com.atguigu.spzx.user.mapper;

import com.atguigu.spzx.model.entity.user.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 77943
* @description 针对表【user_info(会员表)】的数据库操作Mapper
* @createDate 2024-03-20 14:43:29
* @Entity generator.domain.UserInfo
*/
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    UserInfo getByUsername(String username);

    void save(UserInfo userInfo);
}




