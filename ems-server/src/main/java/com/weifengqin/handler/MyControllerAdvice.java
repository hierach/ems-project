package com.weifengqin.handler;

import com.weifengqin.dto.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class MyControllerAdvice {

    /**
     * 捕获全局运行时异常
     * @param e
     * @return 返回错误信息给前台
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result handlerException(Exception e){
        //获取异常信息，存放如ResponseResult的msg属性
        Result result = Result.fail("服务器异常");

        e.printStackTrace();//打印出错信息
        //把result作为返回值返回，要求到时候转换成json存入响应体中
        return result;
    }
}