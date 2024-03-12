package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.ProductService;
import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "商品管理")
@RestController
@RequestMapping(value = "/admin/product/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "分页查询接口")
    @GetMapping( "/{page}/{limit}")
    public Result<Page<Product>> findByPage(@PathVariable Integer page, @PathVariable Integer limit, ProductDto productDto) {
        Page<Product> pageInfo = productService.findByPage(page, limit, productDto);
        return Result.build(pageInfo , ResultCodeEnum.SUCCESS) ;
    }

    @Operation(summary = "保存商品数据")
    @PostMapping("/save")
        public Result save(@RequestBody Product product) {
        productService.saveAll(product);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    @Operation(summary = "修改功能前的数据回显")
    @GetMapping("/getById/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        //不使用mybatis-plus提供的方法 自己定义一个方法
        Product byId = productService.getProductById(id);
        return Result.build(byId , ResultCodeEnum.SUCCESS) ;
    }

    @Operation(summary = "删除商品")
    @DeleteMapping("/deleteById/{id}")
    public Result<Product> deleteById(@Parameter(name = "id", description = "商品id", required = true) @PathVariable Long id) {
        //不使用mybatis-plus提供的方法 自己定义一个方法
         productService.deleteById(id);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    @Operation(summary = "审核商品")
    @GetMapping("/updateAuditStatus/{id}/{auditStatus}")
    public Result updateAuditStatus(@PathVariable Long id, @PathVariable Integer auditStatus) {
        productService.updateAuditStatus(id, auditStatus);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    @Operation(summary = "上下架商品")
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        productService.updateStatus(id, status);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }
}