package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.ProductUnitMapper;
import com.atguigu.spzx.manager.service.ProductUnitService;
import com.atguigu.spzx.model.entity.base.ProductUnit;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductUnitServiceImpl extends ServiceImpl<ProductUnitMapper, ProductUnit> implements ProductUnitService {


    @Override
    public List<ProductUnit> findAll() {
        QueryWrapper<ProductUnit> queryWrapper = new QueryWrapper<>();
        //查询的字段标明
        queryWrapper.select("id","name","create_time“,”update_time“,”is_deleted");
        //按照什么排序
        queryWrapper.orderByAsc("id");
        //无需处理逻辑删除字段，自动处理

        return baseMapper.selectList(null);
    }
}
