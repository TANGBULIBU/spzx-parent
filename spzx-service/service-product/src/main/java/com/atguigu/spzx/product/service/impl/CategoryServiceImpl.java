package com.atguigu.spzx.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.product.mapper.CategoryMapper;
import com.atguigu.spzx.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service

public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private RedisTemplate<String , String> redisTemplate ;

    @Autowired
    private  CategoryMapper categoryMapper;
    @Override
    public List<Category> findOneCategory() {

        /*
        where
        parent_id = 0
        and status = 1
        and is_deleted = 0
        order by order_num*/

        //从redis中查询所有的一级分类数据
        String categoryListJSON = redisTemplate.opsForValue().get("category:one");
        //从redis中获取数据 如果访问的内容存在 就从中获取查询
        if(!StringUtils.isEmpty(categoryListJSON)) {
            List<Category> categoryList = JSON.parseArray(categoryListJSON, Category.class);
            System.out.println("从Redis缓存中查询到了所有的一级分类数据");
            return categoryList ;
        }

        LambdaQueryWrapper<Category> categoryQueryWrapper = new LambdaQueryWrapper<>();
//        使用stream流对条件进行筛选 获取品牌的parentId=0  和status=1的相关数据
        categoryQueryWrapper
                .eq(Category::getParentId, 0)
                .eq(Category::getStatus, 1)
                .orderByAsc(Category::getOrderNum);

        /*
        从数据库中查询所有的一级分类数据
         */
        List<Category> categories = baseMapper.selectList(categoryQueryWrapper);
        System.out.println("从数据库中查询到所有的一级分类");
        redisTemplate.opsForValue().set("category:one", JSON.toJSONString(categories),7, TimeUnit.DAYS);
        return categories;
    }

    //    获取分类树形数据
    @Override
    //添加cache缓存技术注解
    @Cacheable(value = "category" , key = "'all'")
    public List<Category> findCategoryTree() {
        //从数据库获取状态为1的全部信息
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<Category>()
                .eq(Category::getStatus, 1)
                .orderByAsc(Category::getOrderNum);

        List<Category> categories = baseMapper.selectList(categoryLambdaQueryWrapper);

        //获取第一个级别的分类信息
//        filter()方法对Stream中的元素进行筛选，条件是元素的parentId属性的long值等于0。最后使用collect()方法将筛选结果收集到一个新的List中并返
        List<Category> collectsList= categories.stream()
                .filter(item -> item.getParentId().longValue() == 0)
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(categories)) {
            categories.forEach(category -> {
                List<Category> collectsList1 = categories.stream()
                        .filter(item -> item.getParentId()
                                .longValue() == category.getId().longValue()).collect(Collectors.toList());
                //获取第三个级别的分类信息
                if (!CollectionUtils.isEmpty(collectsList1)) {
                    collectsList1.forEach(category2 -> {
                        List<Category> categoryList2 = collectsList1.stream().filter(item -> item.getParentId().longValue() == category2.getId().longValue()).collect(Collectors.toList());
                        category2.setChildren(categoryList2);
                    });
                }
            });
        }

        return collectsList;
    }
}