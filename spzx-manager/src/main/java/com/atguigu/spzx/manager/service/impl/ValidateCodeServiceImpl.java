package com.atguigu.spzx.manager.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.atguigu.spzx.manager.service.ValidateCodeService;
import com.atguigu.spzx.model.vo.system.ValidateCodeVo;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {

    //使用redis接收验证码信息
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public ValidateCodeVo generateValidateCode() {
        //获取验证码本码
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(150, 48, 4, 20);
        String code = circleCaptcha.getCode();
        String imageBase64 = circleCaptcha.getImageBase64();

        //生成uuid
        String Ucode = UUID.randomUUID().toString().replace("-", "");

        //讲验证码存储redis
        redisTemplate.opsForValue().set("user:login:validatecode:" + Ucode, code, 5, TimeUnit.MINUTES);


        ValidateCodeVo vo = new ValidateCodeVo();
        vo.setCodeKey(Ucode);
        vo.setCodeValue("data:image/png;base64," + imageBase64);
        return vo;
    }
}
