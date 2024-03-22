package com.atguigu.spzx.cart.service.impl;

import com.alibaba.fastjson2.JSON;
import com.atguigu.spzx.cart.service.CartService;
import com.atguigu.spzx.feign.product.ProductFeignClient;
import com.atguigu.spzx.model.entity.h5.CartInfo;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.utils.AuthContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ProductFeignClient productFeignClient;


    //添加购物车
    @Override
    public void addToCart(Long skuId, Integer skuNum) {
        // 获取当前登录用户的id 从ThreadLocal中
//        redisTemplate.opsForValue().get("user:cart");

        CartInfo cartInfo=null;
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = getCartKey(userId);


        //先获取redis中存入的数据中的skuId的数据 key是使用getCartKey定义好的值
        Object cartInfoObj = redisTemplate.opsForHash().get(cartKey, skuId);
        // 当购物车中没用该商品的时候，则直接添加到购物车！
        if (cartInfoObj != null) {
            //将json解析为java对象 反序列化 将redis拿到的数据转化为java对象
            cartInfo = JSON.parseObject(cartInfoObj.toString(), CartInfo.class);
            cartInfo.setSkuNum(cartInfo.getSkuNum() + 1);//点击后商品数量增加
            cartInfo.setIsChecked(1);
            cartInfo.setUpdateTime(new Date());
        } else {//购物车没有该商品就添加新的商品

            // 购物车数据是从商品详情得到 {skuInfo}
            cartInfo = new CartInfo();
            ProductSku bySkuId = productFeignClient.getBySkuId(skuId);
            cartInfo.setSkuId(skuId);
            cartInfo.setUserId(userId);
            cartInfo.setCartPrice(bySkuId.getSalePrice());
            cartInfo.setSkuName(bySkuId.getSkuName());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setImgUrl(bySkuId.getThumbImg());
            cartInfo.setIsChecked(1);
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());
        }

        //将cartInfo的信息存入redis
        redisTemplate.opsForHash().put(cartKey, skuId, JSON.toJSONString(cartInfo));
    }

    private String getCartKey(Long userId) {
        //转为redis的格式
        return "user:cart:" + userId;
    }
}


