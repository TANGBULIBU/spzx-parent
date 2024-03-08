package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.CategoryMapper;
import com.atguigu.spzx.manager.service.CategoryService;
import com.atguigu.spzx.model.entity.product.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findByParentId(Long parentId) {

        //查询id下面的子分类数据
        List<Category> categoryList = categoryMapper.selectByParentId(parentId);

        if (!CollectionUtils.isEmpty(categoryList)) {
            //计算处该parentId下是否有子菜单 大于0就是存在
            categoryList.forEach(item -> {
                int count = categoryMapper.countByParentId(item.getId());
                if (count > 0) {
                    //前端所需要的数据 存在的话 前端显示小箭头
                    item.setHasChildren(true);
                } else {
                    item.setHasChildren(false);
                }
            });
        }
        return categoryList;
    }
}
