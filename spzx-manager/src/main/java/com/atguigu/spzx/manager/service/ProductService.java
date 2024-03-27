package com.atguigu.spzx.manager.service;


import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ProductService  extends IService<Product> {
    void saveAll(Product product);

    Product getProductById(Long id);

    Page<Product> findByPage(Integer page, Integer limit, ProductDto productDto);

    void deleteById(Long id);

    void updateAuditStatus(Long id, Integer auditStatus);

    void updateStatus(Long id, Integer status);

}
