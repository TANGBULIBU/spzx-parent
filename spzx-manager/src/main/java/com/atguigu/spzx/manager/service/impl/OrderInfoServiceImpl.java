package com.atguigu.spzx.manager.service.impl;

import cn.hutool.core.date.DateUtil;
import com.atguigu.spzx.manager.mapper.OrderInfoMapper;
import com.atguigu.spzx.manager.mapper.OrderStatisticsMapper;
import com.atguigu.spzx.manager.service.OrderInfoService;
import com.atguigu.spzx.model.dto.order.OrderStatisticsDto;
import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.entity.order.OrderStatistics;
import com.atguigu.spzx.model.vo.order.OrderStatisticsVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Override
    public void insertStatisticsRecord(String orderCreateTime) {

        //根据orderCreateTime查询统计表中的数据是否存在，如果不存在则插入数据(order_statistics) service  mapper
        QueryWrapper<OrderStatistics> orderStatisticsQueryWrapper = new QueryWrapper<>();
        //SELECT * FROM `order_statistics` WHERE `order_date` = '?';
        orderStatisticsQueryWrapper.eq("order_date",orderCreateTime);
        //一对一选择
        OrderStatistics orderStatistics = orderStatisticsMapper.selectOne(orderStatisticsQueryWrapper);

        if (orderStatistics!=null) return;

        //根据orderCreateTime统计order_info表中的订单总金额数据(order_info)
        orderStatistics= orderInfoMapper.selectStatistics(orderCreateTime);

        //然后将计算后的总金额数据写入到统计结果表中(order_statistics)
        if(orderStatistics != null){
            orderStatisticsMapper.insert(orderStatistics);
        }

    }

    @Override
    public OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto) {
        String createTimeBegin = orderStatisticsDto.getCreateTimeBegin();
        String createTimeEnd = orderStatisticsDto.getCreateTimeEnd();

        QueryWrapper<OrderStatistics> orderStatisticsQueryWrapper = new QueryWrapper<>();
        //查询时间范围是大于开始时间 小于结束时间的范围内
        orderStatisticsQueryWrapper.ge("order_date",createTimeBegin);
        orderStatisticsQueryWrapper.le("order_date",createTimeEnd);

        List<OrderStatistics> statisticsList = orderStatisticsMapper.selectList(orderStatisticsQueryWrapper);

//        将数据组装成数值列表
        List<BigDecimal> amountList = statisticsList.stream().map(OrderStatistics::getTotalAmount).collect(Collectors.toList());

//将statisticsList中每一条记录的orderDate属性组装成一个日期字符串列表
        List<String> dateList = statisticsList.stream().map(orderStatistics ->
                DateUtil.format(orderStatistics.getOrderDate(), "yyyy-MM-dd")
        ).collect(Collectors.toList());

        OrderStatisticsVo orderStatisticsVo = new OrderStatisticsVo();
        orderStatisticsVo.setAmountList(amountList);
        orderStatisticsVo.setDateList(dateList);

        return orderStatisticsVo;
    }
}
