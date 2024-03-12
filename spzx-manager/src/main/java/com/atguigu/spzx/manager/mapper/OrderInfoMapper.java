package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
    void insertStatisticsRecord(String orderCreateTime);
}
