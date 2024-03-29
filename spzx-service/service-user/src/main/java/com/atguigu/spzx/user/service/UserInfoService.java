package com.atguigu.spzx.user.service;

import com.atguigu.spzx.model.dto.h5.UserLoginDto;
import com.atguigu.spzx.model.dto.h5.UserRegisterDto;
import com.atguigu.spzx.model.entity.user.UserInfo;
import com.atguigu.spzx.model.vo.h5.UserInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 77943
* @description 针对表【user_info(会员表)】的数据库操作Service
* @createDate 2024-03-20 14:43:29
*/
public interface UserInfoService extends IService<UserInfo> {

    void register(UserRegisterDto userRegisterDto);

    String login(UserLoginDto userLoginDto, String ipAddr);

    UserInfoVo getCurrentUserInfo(String token);
}
