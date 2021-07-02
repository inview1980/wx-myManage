package com.example.mymanage.http;

import com.example.mymanage.tool.RandomUtil;
import com.example.mymanage.tool.StaticConfigData;
import lombok.Getter;

import java.util.Date;


public class VerificationCode {
    @Getter private final String verificationCode;
    private final Date buildDate;
    @Getter private final String key;

    public VerificationCode() {
        buildDate = new Date();
        key= RandomUtil.getString();
        verificationCode= RandomUtil.getString();
    }

    public boolean isOverdue() {
        long time = new Date().getTime() - buildDate.getTime();
        return StaticConfigData.VerificationCodeEffectiveTime <= time;
    }
}
