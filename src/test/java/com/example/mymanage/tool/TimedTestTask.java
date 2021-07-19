package com.example.mymanage.tool;

import com.example.mymanage.GlobalExceptionHandler;
import lombok.SneakyThrows;
import org.junit.Test;


public class TimedTestTask {

    @Test@SneakyThrows
    public void personChanged() {
        GlobalExceptionHandler excep=new GlobalExceptionHandler();
        System.out.println(excep.exceptionHandler(null, new MyException(1000, "j1j1j")));
    }


}