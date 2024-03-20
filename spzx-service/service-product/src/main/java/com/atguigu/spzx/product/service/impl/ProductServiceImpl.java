package com.atguigu.spzx.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.entity.product.ProductDetails;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.vo.h5.ProductItemVo;
import com.atguigu.spzx.product.mapper.ProductDetailsMapper;
import com.atguigu.spzx.product.mapper.ProductMapper;
import com.atguigu.spzx.product.mapper.ProductSkuMapper;
import com.atguigu.spzx.product.service.ProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    // 接口实现类
    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Override
    public ProductItemVo item(Long skuId) {

//      由于在mysql中的部分数据写入时与前端调用时提供的数据不同 所以需要对mysql获取的数据进行拆分
        ProductSku productSku = productSkuMapper.selectById(skuId);
        String skuSpec = productSku.getSkuSpec();
        String[] skuSpecList = skuSpec.split(",");
        //转为list集合
        Stream<String> skuSpecStringStream = Arrays.asList(skuSpecList)
                .stream()
                .map(e -> e.split(":")[1]);

        //转换为集合list set map 使用+拼接
        String skuSpecCollect = skuSpecStringStream.collect(Collectors.joining(" + "));
        productSku.setSkuSpec(skuSpecCollect);

        //商品基本信息 product
        Long productId = productSku.getProductId();
        Product product = baseMapper.selectById(productId);

        //商品轮播图信息 sliderUrlList
        //将逗号分隔的图片字符串转成列表
        List SliderList = Arrays.asList(product.getSliderUrls().split(","));

        //商品详情  图片 detailsImageUrlList
        LambdaQueryWrapper<ProductDetails> productDetailsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        productDetailsLambdaQueryWrapper.eq(ProductDetails::getProductId, productId);
//      一对一的传
        ProductDetails productDetails = productDetailsMapper.selectOne(productDetailsLambdaQueryWrapper);
        //将逗号分隔的图片字符串转成列表
        List<String> productDetailsList = Arrays.asList(productDetails.getImageUrls().split(","));

        //商品规格 specValueList
        String specValue = product.getSpecValue();
        JSONArray specValueJsonArray = JSON.parseArray(specValue);

        //当前商品sku规格属性 productSku
        LambdaQueryWrapper<ProductSku> productSkuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        productSkuLambdaQueryWrapper.eq(ProductSku::getProductId, productId);
        List<ProductSku> productSkus = productSkuMapper.selectList(productSkuLambdaQueryWrapper);
        //获取的值是以map显示的 转换为map
        HashMap<String, Object> skuSpecValueMap = new HashMap<>();
        productSkus.forEach(item -> {
            String[] skuSpecArray = item.getSkuSpec().split(",");
            Stream<String> stringStream = Arrays.asList(skuSpecArray)
                    .stream()
                    .map(e -> e.split(":")[1]);
            String collect = stringStream.collect(Collectors.joining(" + "));
            skuSpecValueMap.put(collect, item.getId());
        });


//        组装ProductItemVo信息
        ProductItemVo productItemVo = new ProductItemVo();
        productItemVo.setProductSku(productSku);
        productItemVo.setProduct(product);
        productItemVo.setSliderUrlList(SliderList);
        productItemVo.setDetailsImageUrlList(productDetailsList);
        productItemVo.setSkuSpecValueMap(skuSpecValueMap);
        productItemVo.setSpecValueList(specValueJsonArray);
        return productItemVo;
    }
}
