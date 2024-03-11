package com.atguigu.spzx.manager.service;


import com.atguigu.spzx.model.entity.product.Product;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ProductService  extends IService<Product> {
    void saveAll(Product product);
//    PageInfo<Product> findByPage(Integer page, Integer limit, ProductDto productDto);
}
