package com.atguigu.spzx.common.exception;


import com.atguigu.spzx.model.vo.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.build(null, 201, e.getMessage());
    }

    @ExceptionHandler(value = GuiguException.class)     // 处理自定义异常
    @ResponseBody
    public Result error(GuiguException e) {
        e.printStackTrace();
        return Result.build(null, e.getCode(), e.getMessage());
    }
}
