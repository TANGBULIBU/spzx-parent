package com.atguigu.spzx.manager.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.atguigu.spzx.manager.mapper.CategoryMapper;
import com.atguigu.spzx.model.vo.product.CategoryExcelVo;

import java.util.ArrayList;
import java.util.List;

public class ExcelListener<T> extends AnalysisEventListener<T> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;

    /**
     * 缓存的数据
     */
    private List cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);


    //此处不想使用spring管理 所以不能使用自动注入 调用mapper对象
    //需要使用构造方法 调mapper 实现对数据库内容的写入
    private CategoryMapper categoryMapper;

    public ExcelListener(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    //空参
    public ExcelListener() {
    }



    @Override
    public void invoke(T o, AnalysisContext analysisContext) {  // 每解析一行数据就会调用一次该方法

        CategoryExcelVo data = (CategoryExcelVo) o;

        cachedDataList.add(data);

        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }




    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // excel解析完毕以后需要执行的代码
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        //此处设置的是满100条记录一次数据 但是如果未满100条的记录 就需要再此处保存
        System.out.println("未满需要一次上传的个数");
        saveData();
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        categoryMapper.save(cachedDataList);
    }


    //可以通过实例获取该值
    private List<T> datas = new ArrayList<>();
    public List<T> getDatas() {
        return datas;
    }
}
