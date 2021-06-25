package com.example.mymanage.http;

import com.example.mymanage.tool.RandomUtil;
import com.example.mymanage.tool.StateData;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import sun.security.timestamp.TimestampToken;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class MyToken {

    @Getter
    private String token = RandomUtil.getString();
    private Date buildTime = new Date();


    public boolean isOverdue() {
        long time = new Date().getTime() - buildTime.getTime();
        return StateData.getUserTokenEffectiveTime() <= time;
    }

    public boolean checkToken(String token) {
        return this.token.equals(token);
    }
}
