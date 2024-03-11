package com.atguigu.spzx.manager.controller;


import com.atguigu.spzx.manager.service.ProductSpecService;
import com.atguigu.spzx.model.entity.product.ProductSpec;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品规格管理")
@RestController
@RequestMapping("/admin/product/productSpec")
public class ProductSpecController {
    @Autowired
    private ProductSpecService productSpecService;

//    @Operation(summary = "分页查询接口")
//    @GetMapping("/{page}/{limit}")
//    public Result<Page<ProductSpec>> findByPage(@PathVariable Integer page, @PathVariable Integer limit){
//        Page<ProductSpec> productSpecs = new Page<>(page, limit);
//        productSpecService.page(productSpecs);
//        return  Result.build(productSpecs, ResultCodeEnum.SUCCESS);
//    }

    @Operation(summary = "分页查询接口")
    @GetMapping("/{page}/{limit}")
    public Result<Page<ProductSpec>> findByPage(@PathVariable Integer page, @PathVariable Integer limit){
        PageInfo<ProductSpec> pageInfo = productSpecService.findByPage(page, limit);
        return Result.build(pageInfo , ResultCodeEnum.SUCCESS) ;
    }

    @Operation(summary = "添加商品规格")
    @PostMapping("/save")
    public Result save(@RequestBody ProductSpec productSpec){
        productSpecService.save(productSpec);
        return  Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "修改商品规格")
    @PutMapping("/updateById")
    public Result updateById(@RequestBody ProductSpec productSpec){
        productSpecService.updateById(productSpec);
        return  Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "删除商品规格")
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Long id){
        productSpecService.deleteById(id);
        return  Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "加载商品规格数据")
    @GetMapping("/findAll")
    public Result findAll(){
        List<ProductSpec> list = productSpecService.findAll();
        return Result.build(list , ResultCodeEnum.SUCCESS) ;

//        QueryWrapper<ProductSpec> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
//
//        List<ProductSpec> list = productSpecService.list(queryWrapper);
//        return Result.build(list,ResultCodeEnum.SUCCESS);
    }


}
