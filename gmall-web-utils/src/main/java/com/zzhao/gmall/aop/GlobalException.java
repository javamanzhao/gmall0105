package com.zzhao.gmall.aop;

import groovy.util.logging.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author Administrator
 * @date 2019/11/28 0028下午 16:36
 */
@Slf4j
@ControllerAdvice
public class GlobalException {


    @ExceptionHandler(value = Exception.class)  //申明捕获那个异常类
    @ResponseBody  //返回给浏览器的是一个json格式，上面又没有@RestController，所以在此申明@ResponseBody
    public String  handle(Exception e) {
        e.printStackTrace();
        return "程序内部错误,请联系管理员！";
    }
}
