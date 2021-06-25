package com.example.mymanage;

import com.example.mymanage.http.Result;
import com.example.mymanage.tool.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice@Slf4j
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(HttpServletRequest request, Exception e){
        log.error("{}:{}",e.toString(),e.getLocalizedMessage());
        if(e instanceof MyException){
            MyException ess=(MyException)e;
            return Result.Error(ess.getCode(),ess.getLocalizedMessage());
        }
        String  tmp=e.toString();
        return Result.Error(10001,tmp);
    }
}
