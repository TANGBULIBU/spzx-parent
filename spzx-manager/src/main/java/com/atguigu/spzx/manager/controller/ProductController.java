package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.ProductService;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "商品管理")
@RestController
@RequestMapping(value = "/admin/product/product")
public class ProductController {

    @Autowired
    private ProductService productService;

//    @Operation(summary = "分页查询接口")
//    @GetMapping( "/{page}/{limit}")
//    public Result<PageInfo<Product>> findByPage(@PathVariable Integer page, @PathVariable Integer limit, ProductDto productDto) {
//        PageInfo<Product> pageInfo = productService.findByPage(page, limit, productDto);
//        return Result.build(pageInfo , ResultCodeEnum.SUCCESS) ;
//    }

    @Operation(summary = "保存商品数据")
    @PostMapping("/save")
        public Result save(@RequestBody Product product) {
        productService.saveAll(product);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

}