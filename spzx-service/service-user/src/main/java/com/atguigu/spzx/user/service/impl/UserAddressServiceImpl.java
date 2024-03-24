package com.atguigu.spzx.user.service.impl;

import com.atguigu.spzx.model.entity.user.UserAddress;
import com.atguigu.spzx.user.mapper.UserAddressMapper;
import com.atguigu.spzx.user.service.UserAddressService;
import com.atguigu.spzx.utils.AuthContextUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {



    @Override
    public List<UserAddress> findUserAddressList() {

        // 获取当前登录用户的id
        Long id = AuthContextUtil.getUserInfo().getId();
        LambdaQueryWrapper<UserAddress> userAddressLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userAddressLambdaQueryWrapper.eq(UserAddress::getUserId, id);
        return baseMapper.selectList(userAddressLambdaQueryWrapper);
    }
}
