package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.ProductUnitMapper;
import com.atguigu.spzx.manager.service.ProductUnitService;
import com.atguigu.spzx.model.entity.base.ProductUnit;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductUnitServiceImpl extends ServiceImpl<ProductUnitMapper, ProductUnit> implements ProductUnitService {

    @Override
    public List<Map<String, Object>> listAll() {

        //queryWrapper 中有多种方法 排序或者
        QueryWrapper<ProductUnit> queryWrapper = new QueryWrapper<>();
        //查询的字段标明
        queryWrapper.select("id","name");
        //按照什么排序
        queryWrapper.orderByAsc("id");
        //无需处理逻辑删除字段，自动处理
        List<Map<String, Object>> maps = baseMapper.selectMaps(queryWrapper);

        return maps;
    }
}
