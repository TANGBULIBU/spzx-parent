package com.atguigu.spzx.manager.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.spzx.common.exception.GuiguException;
import com.atguigu.spzx.manager.listener.ExcelListener;
import com.atguigu.spzx.manager.mapper.CategoryMapper;
import com.atguigu.spzx.manager.service.CategoryService;
import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.product.CategoryExcelVo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
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

    @Override
    public void exportData(HttpServletResponse response) {
        try {
            //设置响应结果
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");

            String fileName = URLEncoder.encode("尚品甄选分类数据", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            // 查询数据库中的数据
            List<Category> categoryList = categoryMapper.selectAll();
            List<CategoryExcelVo> categoryExcelVoList = new ArrayList<>(categoryList.size());

            for (Category category : categoryList) {
                CategoryExcelVo categoryExcelVo = new CategoryExcelVo();
                BeanUtils.copyProperties(category, categoryExcelVo, CategoryExcelVo.class);
                categoryExcelVoList.add(categoryExcelVo);
            }

            // 写出数据到浏览器端
            EasyExcel.write(response.getOutputStream(), CategoryExcelVo.class).sheet("分类数据").doWrite(categoryExcelVoList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importData(MultipartFile file) {

        //读数据
        try {
            //创建监听器对象 传递mapper 给监听器
            ExcelListener<CategoryExcelVo> excelListener = new ExcelListener<>(categoryMapper);
            // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
            //获取文件路径file.getInputStream()
            EasyExcel.read(file.getInputStream(), CategoryExcelVo.class, excelListener).sheet().doRead();
        } catch (IOException e) {
            //数据出现异常
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }

    }
}
