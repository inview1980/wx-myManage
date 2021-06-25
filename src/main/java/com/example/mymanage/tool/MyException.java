package com.example.mymanage.tool;

import com.example.mymanage.http.HttpResultEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.function.Supplier;

@Data@EqualsAndHashCode(callSuper = false)
public  class  MyException extends RuntimeException implements Supplier<MyException> {
    private int code;

    public MyException(int code, String codeMsg) {
        super(codeMsg);
        this.code = code;

    }

    public MyException(HttpResultEnum resultEnum){
        super(resultEnum.getCodeMsg());
        this.code=resultEnum.getCode();
    }

    @Override
    public MyException get() {
        return this;
    }
}
