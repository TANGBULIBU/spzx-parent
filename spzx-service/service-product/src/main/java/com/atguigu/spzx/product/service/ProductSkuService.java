package com.atguigu.spzx.product.service;

import com.atguigu.spzx.model.dto.h5.ProductSkuDto;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ProductSkuService extends IService<ProductSku> {
    List<ProductSku> findProductSkuBySale();

    IPage<ProductSku> findByPage(Integer page, Integer limit, ProductSkuDto productSkuDto);

}