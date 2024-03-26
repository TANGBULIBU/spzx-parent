package com.atguigu.spzx.pay.service.impl;

import com.alibaba.fastjson2.JSON;
import com.atguigu.spzx.feign.order.OrderFeignClient;
import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.entity.pay.PaymentInfo;
import com.atguigu.spzx.pay.mapper.PaymentInfoMapper;
import com.atguigu.spzx.pay.service.PaymentInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {


    @Autowired
    private OrderFeignClient orderFeignClient;

    @Override
    @Transactional
    public PaymentInfo savePaymentInfo(String orderNo) {

        //防止重复点击支付按钮生成多条paymentInfo记录，先对paymentInfo进行查询如果存在则不生成相应的记录
        PaymentInfo paymentInfo = this.getPaymentInfoByOrderNo(orderNo);

        //是否能够查询到支付数据
        if (paymentInfo != null) {
            return paymentInfo;
        }

        //获取orderInfo
        OrderInfo orderInfo = orderFeignClient.getOrderInfoByOrderNo(orderNo).getData();

        //创建paymentInfo
        paymentInfo = new PaymentInfo();

        //组装paymentInfo
        paymentInfo.setPaymentStatus(0);//支付状态 ：未支付
        paymentInfo.setAmount(orderInfo.getTotalAmount());//总金额
        paymentInfo.setOrderNo(orderNo);
        paymentInfo.setPayType(orderInfo.getPayType());
        paymentInfo.setUserId(orderInfo.getUserId());

        String content = orderInfo.getOrderItemList()
                .stream()
                .map(orderItem -> orderItem.getSkuName())
                .collect(Collectors.joining(" "));
        paymentInfo.setContent(content);

        //插入到数据库
        baseMapper.insert(paymentInfo);

        //返回给上游业务
        return paymentInfo;
    }

    @Override
    @Transactional
    public void updatePaymentStatus(Map<String, String> paramMap, Integer payType) {
        //查询PaymentInfo

        PaymentInfo paymentInfo = this.getPaymentInfoByOrderNo(paramMap.get("out_trade_no"));
        //更新支付信息
        if (paymentInfo.getPaymentStatus() == 1) {
            return;
        }

        //更新支付信息
        paymentInfo.setPaymentStatus(1);
        paymentInfo.setOutTradeNo(paramMap.get("trade_no"));
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setCallbackContent(JSON.toJSONString(paramMap));
        baseMapper.updateById(paymentInfo);

    }

    public PaymentInfo getPaymentInfoByOrderNo(String orderNo) {
        LambdaQueryWrapper<PaymentInfo> paymentInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //获取订单号
        paymentInfoLambdaQueryWrapper.eq(PaymentInfo::getOrderNo, orderNo);
        PaymentInfo paymentInfo = baseMapper.selectOne(paymentInfoLambdaQueryWrapper);//mp查询相关条件
        return paymentInfo;//返回给支付信息
    }
}