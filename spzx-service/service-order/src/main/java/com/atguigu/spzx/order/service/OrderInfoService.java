package com.atguigu.spzx.order.service;

import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.vo.h5.TradeVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrderInfoService extends IService<OrderInfo> {
    TradeVo getTrade();

}
