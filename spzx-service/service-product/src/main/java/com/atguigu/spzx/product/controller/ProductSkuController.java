package com.atguigu.spzx.product.controller;

import com.atguigu.spzx.model.dto.h5.ProductSkuDto;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.product.service.ProductSkuService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "商品列表管理")
@RestController
@RequestMapping(value="/api/product")
public class ProductSkuController {

    @Autowired
    private ProductSkuService productSkuService;

    @Operation(summary = "分页查询")
    @GetMapping(value = "/{page}/{limit}")
    public Result<IPage<ProductSku>> findByPage(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Integer page,

            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Integer limit,

            @Parameter(name = "productSkuDto", description = "搜索条件对象", required = false)
            ProductSkuDto productSkuDto) {

        IPage<ProductSku> pageModel = productSkuService.findByPage(page, limit, productSkuDto);
        return Result.build(pageModel , ResultCodeEnum.SUCCESS) ;
    }
}
