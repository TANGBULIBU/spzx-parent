package com.atguigu.spzx.product.service;

import com.atguigu.spzx.model.entity.product.ProductSku;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ProductSkuService extends IService<ProductSku> {
    List<ProductSku> findProductSkuBySale();
}