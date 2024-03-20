package com.atguigu.spzx.product.service.impl;

import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.product.mapper.BrandMapper;
import com.atguigu.spzx.product.service.BrandService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 77943
 * @description 针对表【brand(分类品牌)】的数据库操作Service实现
 * @createDate 2024-03-19 21:04:01
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand>
        implements BrandService {

    @Override
    public List<Brand> findAll() {
        LambdaQueryWrapper<Brand> brandLambdaQueryWrapper = new LambdaQueryWrapper<Brand>()
                .orderByDesc(Brand::getId);//通过id值默认降序

        return baseMapper.selectList(brandLambdaQueryWrapper);
    }
}




