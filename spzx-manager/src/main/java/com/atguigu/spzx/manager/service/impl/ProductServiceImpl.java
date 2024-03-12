package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.ProductDetailsMapper;
import com.atguigu.spzx.manager.mapper.ProductMapper;
import com.atguigu.spzx.manager.mapper.ProductSkuMapper;
import com.atguigu.spzx.manager.service.ProductService;
import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.entity.product.ProductDetails;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @Autowired
    private  ProductMapper productMapper;


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

    @Transactional
    @Override
    public Product getProductById(Long id) {

        //selectById 根据id批量查询
        Product product = baseMapper.selectProductById(id);

        //使用queryWrapper 完成对数据库的相关操作 泛型类型是Sku
        //此处查询会因为实体类中的部分字段 多余 产生问题 应该使用之前的在mapper中的方法
        QueryWrapper<ProductSku> queryWrapperSku = new QueryWrapper<>();
//        where product_id=#{productId}
        queryWrapperSku.eq("product_id",id);
//        order by id desc
        queryWrapperSku.orderByDesc("id");
        List<ProductSku> productSkusList = productSkuMapper.selectList(queryWrapperSku);
        //根据id设置其list数组和相关图片的回显
        product.setProductSkuList(productSkusList);


//        查询图片详细数据
        //还可以使用一种方法 不需要再次查询 注解修改
        QueryWrapper<ProductDetails> productDetailsQueryWrapper = new QueryWrapper<>();
        //根据productId获取图片数据
//        productDetailsQueryWrapper.eq("product_id",product.getId());
//        ProductDetails productDetails = new ProductDetails();
//        productDetails.setImageUrls(detailsImageUrls);
//        productDetailsMapper.update(productDetails,productDetailsQueryWrapper);

        productDetailsQueryWrapper.eq("product_id", id);
        ProductDetails productDetails = productDetailsMapper.selectOne(productDetailsQueryWrapper);
        product.setDetailsImageUrls(productDetails.getImageUrls());
        return product;
    }


    /**
     * 分页查询功能
     */
    @Override
    public Page<Product> findByPage(Integer page, Integer limit, ProductDto productDto) {
//        Page<Product> pageModel = PageHelper.startPage(page, limit);
//        List<Product> productList=productMapper.findByPage(productDto);
//        return new PageInfo<>(productList);
        Page<Product> pageModel = new Page<>(page, limit);

        //自定义mapper拓展方法
         List<Product> records= baseMapper.findListByPage(pageModel, productDto);
        pageModel.setRecords(records);
        return pageModel;

    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        //删除三个表数据
        //调用baseMapper 获取删除商品的id
        baseMapper.deleteById(id);

        LambdaQueryWrapper<ProductSku> productSkuQueryWrapper = new LambdaQueryWrapper<>();
        //从实体类中获取id
        productSkuQueryWrapper.eq(ProductSku::getProductId,id);
        productSkuMapper.delete(productSkuQueryWrapper);


        LambdaQueryWrapper<ProductDetails> productDetailsQueryWrapper = new LambdaQueryWrapper<>();
        //从实体类中获取id
        productDetailsQueryWrapper.eq(ProductDetails::getProductId,id);
        productDetailsMapper.delete(productDetailsQueryWrapper);

    }

    @Override
    public void updateAuditStatus(Long id, Integer auditStatus) {
        Product product = new Product();
        product.setId(id);

        if (auditStatus==1){
            product.setAuditStatus(1);
            product.setAuditMessage("审批通过");
        }else {
            product.setAuditStatus(-1);
            product.setAuditMessage("审批未通过");
        }
        baseMapper.updateById(product);
    }

    /**
     * 商品上下架
     * @param id
     * @param status
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        Product product = new Product();
        product.setId(id);

        if (status==1){
            product.setStatus(1);
        }else {
            product.setStatus(-1);
        }
        baseMapper.updateById(product);
    }

}
