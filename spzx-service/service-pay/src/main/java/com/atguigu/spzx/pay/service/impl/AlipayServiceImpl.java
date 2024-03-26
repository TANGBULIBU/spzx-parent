package com.atguigu.spzx.pay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.atguigu.spzx.common.exception.GuiguException;
import com.atguigu.spzx.model.entity.pay.PaymentInfo;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.pay.properties.AlipayProperties;
import com.atguigu.spzx.pay.service.AlipayService;
import com.atguigu.spzx.pay.service.PaymentInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Autowired
    private AlipayProperties alipayProperties;


    @Override
    public String submitAlipay(String orderNo) {

        //保存支付记录
        PaymentInfo paymentInfo = paymentInfoService.savePaymentInfo(orderNo);

        //调用阿里支付接口
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();

        model.setOutTradeNo(paymentInfo.getOrderNo());
        model.setSubject(paymentInfo.getContent());
        model.setTotalAmount(paymentInfo.getAmount().toString());

        //手机网站支付
        model.setProductCode("QUICK_WAP_WAY");
        alipayRequest.setBizModel(model);

        // 获取异步地址并设置
        alipayRequest.setNotifyUrl(alipayProperties.getNotifyPaymentUrl());
        // 设置同步地址
        alipayRequest.setReturnUrl(alipayProperties.getReturnPaymentUrl());

        // form表单生产
        String form = "";
        try {
            // 调用SDK生成表单
            AlipayTradeWapPayResponse alipayTradeWapPayResponse = alipayClient.pageExecute(alipayRequest);

            if (alipayTradeWapPayResponse.isSuccess()) {
                System.out.println("调用成功");
                form = alipayTradeWapPayResponse.getBody();
            } else {
                System.out.println("调用失败");
                throw new GuiguException(ResultCodeEnum.DATA_ERROR);
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }

        log.warn(form);

        return form;
    }

}
