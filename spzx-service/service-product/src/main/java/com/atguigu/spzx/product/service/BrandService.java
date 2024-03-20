package com.atguigu.spzx.product.service;

import com.atguigu.spzx.model.entity.product.Brand;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 77943
* @description 针对表【brand(分类品牌)】的数据库操作Service
* @createDate 2024-03-19 21:04:01
*/
public interface BrandService extends IService<Brand> {

    List<Brand> findAll();

}
