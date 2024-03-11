package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.ProductDetailsMapper;
import com.atguigu.spzx.manager.mapper.ProductMapper;
import com.atguigu.spzx.manager.mapper.ProductSkuMapper;
import com.atguigu.spzx.manager.service.ProductService;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.entity.product.ProductDetails;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper,Product> implements ProductService {

    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    @Transactional
    @Override
    public void saveAll(Product product) {
//保存商品数据
        /*product.setStatus(0);
        product.setAuditStatus(0);*/
        //可以直接调用baseMapper
        baseMapper.insert(product);

        //保存商品SKU数据 将数据存入sky中
        List<ProductSku> productSkuList = product.getProductSkuList();
        for(int i=0,size=productSkuList.size(); i<size; i++) {

            //获取从前端封装的SKU对象
            ProductSku productSku = productSkuList.get(i);
            productSku.setSkuCode(product.getId() + "_" + i);       // 构建skuCode
            productSku.setProductId(product.getId());
            productSku.setSkuName(product.getName() + productSku.getSkuSpec());
            productSku.setSaleNum(0); //设置销量
            productSku.setStatus(0); //
            productSkuMapper.insert(productSku);
        }

        //保存商品详情信息
        ProductDetails productDetails = new ProductDetails();
        productDetails.setProductId(product.getId());
        productDetails.setImageUrls(product.getDetailsImageUrls());
        productDetailsMapper.insert(productDetails);
    }




//    @Override
//    public PageInfo<Product> findByPage(Integer page, Integer limit, ProductDto productDto) {
//        PageHelper.startPage(page, limit);
//        List<Product> productList=productMapper.findByPage(productDto);
//        return new PageInfo<>(productList);
//    }

}
