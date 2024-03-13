package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.common.log.service.AsyncOperLogService;
import com.atguigu.spzx.manager.mapper.SysOperLogMapper;
import com.atguigu.spzx.model.entity.system.SysOperLog;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements AsyncOperLogService {
    @Async //异步 LogAspect 和 AsyncOperLogServiceImpl 异步执行
    @Override
    public void saveSysOperLog(SysOperLog sysOperLog) {
        log.info("AsyncOperLogServiceImpl saveSysOperLog");
        baseMapper.insert(sysOperLog);
    }
}