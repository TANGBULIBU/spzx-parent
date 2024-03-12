package com.atguigu.spzx.manager.task;

import cn.hutool.core.date.DateUtil;
import com.atguigu.spzx.manager.mapper.OrderInfoMapper;
import com.atguigu.spzx.manager.mapper.OrderStatisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderStatisticsTask {

    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Scheduled(cron = "0 0 2 * * *")
    public void orderTotalAmountStatisticsTask(){

        //调出前一天的所有数据
        //hutools工具类 获得日期时间
        String orderCreateTime = DateUtil.offsetDay(new Date(), -1).toString(new SimpleDateFormat("yyyy-MM-dd"));

        //查询统计表中数据是否存在 不存在就插入数据
        orderInfoMapper.insertStatisticsRecord( orderCreateTime);

        //统计表中数据

    }
}
