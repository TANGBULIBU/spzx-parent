package com.atguigu.spzx.product.service.impl;

import com.atguigu.spzx.model.dto.h5.ProductSkuDto;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.product.mapper.ProductSkuMapper;
import com.atguigu.spzx.product.service.ProductSkuService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSkuServiceImpl extends ServiceImpl<ProductSkuMapper, ProductSku> implements ProductSkuService {
    @Override
    public List<ProductSku> findProductSkuBySale() {
        return baseMapper.findProductSkuBySale();

    }

    @Override
    public IPage<ProductSku> findByPage(Integer page, Integer limit, ProductSkuDto productSkuDto) {
        //mp的分页插件
        Page<ProductSku> pageModel = new Page<>(page, limit);
//        根据查询条件进行分页查询
        List<ProductSku> productSkuList = baseMapper.findByPage(pageModel, productSkuDto);
        pageModel.setRecords(productSkuList);

        return pageModel;
    }


}