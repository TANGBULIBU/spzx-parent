package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    Product selectProductById(Long id);

    /**
     *
     * @param pageModel
     * @param productDto
     * @return
     */
    List<Product> findListByPage(Page<Product> pageModel, ProductDto productDto);
//    List<Product> findByPage(ProductDto productDto);
}
