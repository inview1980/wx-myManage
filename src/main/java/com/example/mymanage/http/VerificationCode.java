package com.example.mymanage.http;

import com.example.mymanage.tool.RandomUtil;
import com.example.mymanage.tool.StateData;
import lombok.Data;
import lombok.Getter;

import java.util.Date;
import java.util.Random;


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
        return StateData.getVerificationCodeEffectiveTime() <= time;
    }
}
