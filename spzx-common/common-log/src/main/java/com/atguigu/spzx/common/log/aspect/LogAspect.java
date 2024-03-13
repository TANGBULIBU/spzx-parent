package com.atguigu.spzx.common.log.aspect;

import com.atguigu.spzx.common.log.annotation.Log;
import com.atguigu.spzx.common.log.service.AsyncOperLogService;
import com.atguigu.spzx.common.log.util.LogUtil;
import com.atguigu.spzx.model.entity.system.SysOperLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {            // 环绕通知切面类定义

    @Autowired
    private AsyncOperLogService asyncOperLogService ;

    //定义一个环绕通知
    @Around(value = "@annotation(sysLog)")
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint , Log sysLog) {
//        获取属性名称
        String title = sysLog.title();

        SysOperLog sysOperLog = new SysOperLog();

        log.info("LogAspect...doAroundAdvice方法执行了"+title);
        LogUtil.beforeHandleLog(sysLog,joinPoint,sysOperLog);
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();              // 执行业务方法
//            LogUtil.afterHandlLog(sysLog,proceed,sysOperLog,0,null);
        } catch (Throwable e) {                         // 代码执行进入到catch中，业务方法执行产生异常
             e.fillInStackTrace();//统一异常处理
//            throw new RuntimeException(e);
            throw new RuntimeException();//可以选择将e传入RuntimeException中 以获取异常信息
            //如果使用e.fillInStackTrace()打印异常 就不需要在抛出异常处传值
        }
        asyncOperLogService.saveSysOperLog(sysOperLog);

        log.info("LogAspect...doAroundAdvice执行结束！！！！"+title);
        return proceed ;                                // 返回执行结果


    }
}