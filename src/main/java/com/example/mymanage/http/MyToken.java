package com.example.mymanage.http;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
import com.example.mymanage.tool.RandomUtil;
import com.example.mymanage.tool.StaticConfigData;
import lombok.Getter;

import java.util.Date;

@Getter
public class MyToken {
    private static final String dateFormat = "yyyy-M-d";

    private String token = RandomUtil.getString();

    @DateTimeFormat(dateFormat)@JSONField(format = dateFormat)
    private Date buildTime = new Date();


    public boolean isOverdue() {
        long time = new Date().getTime() - buildTime.getTime();
        return StaticConfigData.UserTokenEffectiveTime <= time;
    }

    public boolean checkToken(String token) {
        return this.token.equals(token);
    }
}
