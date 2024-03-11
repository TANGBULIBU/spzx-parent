package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.product.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
//    List<Product> findByPage(ProductDto productDto);
}
