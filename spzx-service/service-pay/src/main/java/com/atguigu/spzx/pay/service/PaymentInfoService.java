package com.atguigu.spzx.pay.service;

import com.atguigu.spzx.model.entity.pay.PaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface PaymentInfoService extends IService<PaymentInfo> {
    PaymentInfo savePaymentInfo(String orderNo);


    void updatePaymentStatus(Map<String, String> paramMap, Integer payType);

    PaymentInfo getPaymentInfoByOrderNo(String orderNo);
}