package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.entity.order.OrderStatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper

public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
    OrderStatistics selectStatistics(String orderCreateTime);

    // 查询指定日期产生的订单数据


}
