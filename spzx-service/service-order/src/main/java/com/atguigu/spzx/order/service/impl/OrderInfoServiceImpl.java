package com.atguigu.spzx.order.service.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.atguigu.spzx.common.exception.GuiguException;
import com.atguigu.spzx.feign.cart.CartFeignClient;
import com.atguigu.spzx.feign.product.ProductFeignClient;
import com.atguigu.spzx.feign.user.UserFeignClient;
import com.atguigu.spzx.model.dto.h5.OrderInfoDto;
import com.atguigu.spzx.model.entity.h5.CartInfo;
import com.atguigu.spzx.model.entity.order.OrderInfo;
import com.atguigu.spzx.model.entity.order.OrderItem;
import com.atguigu.spzx.model.entity.order.OrderLog;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.entity.user.UserAddress;
import com.atguigu.spzx.model.entity.user.UserInfo;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.h5.TradeVo;
import com.atguigu.spzx.order.mapper.OrderInfoMapper;
import com.atguigu.spzx.order.mapper.OrderItemMapper;
import com.atguigu.spzx.order.mapper.OrderLogMapper;
import com.atguigu.spzx.order.service.OrderInfoService;
import com.atguigu.spzx.utils.AuthContextUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    @Autowired
    private CartFeignClient cartFeignClient;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderLogMapper orderLogMapper;

    @Override
    public TradeVo getTrade() {

        //获取购物车列表数据
        List<CartInfo> cartInfoList = cartFeignClient.getAllChecked();
        ArrayList<OrderItem> orderItemList = new ArrayList<>();
        //将购物车数据转换为成功订单明细
        for (CartInfo cartInfo : cartInfoList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setSkuId(cartInfo.getSkuId());
            orderItem.setSkuName(cartInfo.getSkuName());
            orderItem.setSkuNum(cartInfo.getSkuNum());
            orderItem.setSkuPrice(cartInfo.getCartPrice());
            orderItem.setThumbImg(cartInfo.getImgUrl());
            orderItemList.add(orderItem);
        }

        //计算总金额
        BigDecimal totalAmount = new BigDecimal(0);
        for (OrderItem orderItem : orderItemList) {
            totalAmount = totalAmount.add(orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuNum())));
        }
        TradeVo tradeVo = new TradeVo();
        tradeVo.setTotalAmount(totalAmount);
        tradeVo.setOrderItemList(orderItemList);

        return tradeVo;
    }

    @Transactional
    @Override
    public Long submitOrder(OrderInfoDto orderInfoDto) {
        // 数据校验
        List<OrderItem> orderItemList = orderInfoDto.getOrderItemList();
        if (CollectionUtils.isEmpty(orderItemList)) {
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }

        for (OrderItem orderItem : orderItemList) {
            ProductSku productSku = productFeignClient.getBySkuId(orderItem.getSkuId());
            if (null == productSku) {
                throw new GuiguException(ResultCodeEnum.DATA_ERROR);
            }
            //校验库存
            if (orderItem.getSkuNum().intValue() > productSku.getStockNum().intValue()) {
                throw new GuiguException(ResultCodeEnum.STOCK_LESS);
            }
        }

        // 构建订单数据，保存订单
        UserInfo userInfo = AuthContextUtil.getUserInfo();
        OrderInfo orderInfo = new OrderInfo();
        //订单编号
        orderInfo.setOrderNo(String.valueOf(System.currentTimeMillis()));
        //用户id
        orderInfo.setUserId(userInfo.getId());
        //用户昵称
        orderInfo.setNickName(userInfo.getNickName());
        //用户收货地址信息
        UserAddress userAddress = userFeignClient.getUserAddress(orderInfoDto.getUserAddressId());
        orderInfo.setReceiverName(userAddress.getName());
        orderInfo.setReceiverPhone(userAddress.getPhone());
        orderInfo.setReceiverTagName(userAddress.getTagName());
        orderInfo.setReceiverProvince(userAddress.getProvinceCode());
        orderInfo.setReceiverCity(userAddress.getCityCode());
        orderInfo.setReceiverDistrict(userAddress.getDistrictCode());
        orderInfo.setReceiverAddress(userAddress.getFullAddress());
        //订单金额
        BigDecimal totalAmount = new BigDecimal(0);
        for (OrderItem orderItem : orderItemList) {
            totalAmount = totalAmount.add(orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuNum())));
        }
        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setCouponAmount(new BigDecimal(0));
        orderInfo.setOriginalTotalAmount(totalAmount);
        orderInfo.setFeightFee(orderInfoDto.getFeightFee());
        orderInfo.setPayType(2);
        orderInfo.setOrderStatus(0);
        baseMapper.insert(orderInfo);

        //保存订单明细
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderId(orderInfo.getId());
            orderItemMapper.insert(orderItem);
        }

        //记录日志
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderInfo.getId());
        orderLog.setProcessStatus(0);
        orderLog.setNote("提交订单");
        orderLogMapper.insert(orderLog);

        // TODO 远程调用service-cart微服务接口清空购物车数据
        cartFeignClient.deleteChecked();
        return orderInfo.getId();
    }

    @Override
    public TradeVo buy(Long skuId) {
        // 查询商品
        ProductSku productSku = productFeignClient.getBySkuId(skuId);
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setSkuId(skuId);
        orderItem.setSkuName(productSku.getSkuName());
        orderItem.setSkuNum(1);
        orderItem.setSkuPrice(productSku.getSalePrice());
        orderItem.setThumbImg(productSku.getThumbImg());
        orderItemList.add(orderItem);

        // 计算总金额
        BigDecimal totalAmount = productSku.getSalePrice();
        TradeVo tradeVo = new TradeVo();
        tradeVo.setTotalAmount(totalAmount);
        tradeVo.setOrderItemList(orderItemList);

        // 返回
        return tradeVo;
    }

    @Override
    public Page<OrderInfo> findUserPage(Integer page, Integer limit, Integer orderStatus) {
        //分页
        Page<OrderInfo> pageModel = new Page<>(page, limit);

        //获取当前用户id
        Long userId = AuthContextUtil.getUserInfo().getId();

        //根据当前用户id和订单状态获取订单列表
        LambdaQueryWrapper<OrderInfo> orderInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderInfoLambdaQueryWrapper
                .eq(OrderInfo::getUserId, userId)
                //orderStatus不等于null值，再封装条件，否则查询所有订单
                .eq(orderStatus != null, OrderInfo::getOrderStatus, orderStatus)
                .orderByDesc(OrderInfo::getId);
        baseMapper.selectPage(pageModel, orderInfoLambdaQueryWrapper);

        //获取每个订单的订单项列表
        pageModel.getRecords().forEach(orderInfo -> {
            LambdaQueryWrapper<OrderItem> orderItemLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderItemLambdaQueryWrapper
                    .eq(OrderItem::getOrderId, orderInfo.getId())
                    .orderByDesc(OrderItem::getId);
            List<OrderItem> orderItem = orderItemMapper.selectList(orderItemLambdaQueryWrapper);
            orderInfo.setOrderItemList(orderItem);
        });

        //返回分页对象
        return pageModel;
    }

    @Override
    public OrderInfo getByOrderNo(String orderNo) {
        //根据订单获取订单
        LambdaQueryWrapper<OrderInfo> orderInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderInfoLambdaQueryWrapper.eq(OrderInfo::getOrderNo, orderNo);
        //一对一的关系
        OrderInfo orderInfo = baseMapper.selectOne(orderInfoLambdaQueryWrapper);

        //通过orderId获取orderItem列表
        List<OrderItem> orderItemList = this.findOrderItemByOrderId(orderInfo);
        orderInfo.setOrderItemList(orderItemList);

        return orderInfo;
    }

    @Override
    public void updateOrderStatus(String orderNo, Integer orderStatus) {
        LambdaQueryWrapper<OrderInfo> orderInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderInfoLambdaQueryWrapper
                .eq(OrderInfo::getOrderNo, orderNo);
        OrderInfo orderInfo = baseMapper.selectOne(orderInfoLambdaQueryWrapper);
        orderInfo.setOrderStatus(orderStatus);
        orderInfo.setPaymentTime(new Date());
        //还是需要获取orderInfo内容
        baseMapper.updateById(orderInfo);

        //记录日志
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderInfo.getId());
        orderLog.setProcessStatus(1);
        orderLog.setNote("支付宝支付成功");
        //将封装数据插入数据库
        orderLogMapper.insert(orderLog);

    }

    private List<OrderItem> findOrderItemByOrderId(OrderInfo orderInfo) {
        Long orderInfoId = orderInfo.getId();
        LambdaQueryWrapper<OrderItem> orderItemLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderItemLambdaQueryWrapper
                .eq(OrderItem::getOrderId, orderInfoId)
                .orderByDesc(OrderItem::getId);
        return orderItemMapper.selectList(orderItemLambdaQueryWrapper);
    }
}
