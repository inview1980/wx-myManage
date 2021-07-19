package com.example.mymanage.http;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
import com.example.mymanage.tool.RandomUtil;
import com.example.mymanage.tool.StaticConfigData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter@Setter
public class MyToken {
    private String token=RandomUtil.getString();

    @DateTimeFormat(StaticConfigData.DateFormatString)
    @JSONField(format = StaticConfigData.DateFormatString)
    private Date buildTime=new Date();


    @JSONField(serialize = false)
    public boolean isOverdue() {
        long time = new Date().getTime() - buildTime.getTime();
        return StaticConfigData.UserTokenEffectiveTime <= time;
    }

    public boolean checkToken(String token) {
        return this.token.equals(token);
    }
}
