package com.atguigu.spzx.product.service;

import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.vo.h5.ProductItemVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ProductService extends IService<Product> {
    ProductItemVo item(Long skuId);
}
