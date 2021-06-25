package com.example.mymanage.http;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    private int code;
    private String message;
    private Object data;

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Result Ok() {
        return new Result(0, "成功");
    }

    public static Result Ok(Object data) {
        return new Result(0, "成功", data);
    }

    public static Result Error(int code, String msg) {
        return new Result(code, msg);
    }

    public static Result Error(HttpResultEnum resultEnum) {
        return new Result(resultEnum.getCode(), resultEnum.getCodeMsg());
    }
}
