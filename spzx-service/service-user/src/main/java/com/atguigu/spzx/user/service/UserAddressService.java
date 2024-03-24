package com.atguigu.spzx.user.service;

import com.atguigu.spzx.model.entity.user.UserAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserAddressService extends IService<UserAddress> {
    List<UserAddress> findUserAddressList();

}
