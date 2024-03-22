package com.atguigu.spzx.feign.product;

import com.atguigu.spzx.model.entity.product.ProductSku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//微服务远程调用接口
@FeignClient(value = "service-product")
public interface ProductFeignClient {

    @GetMapping("/api/product/productsku/getBySkuId/{skuId}")
    ProductSku getBySkuId(@PathVariable("skuId") Long skuId);
}