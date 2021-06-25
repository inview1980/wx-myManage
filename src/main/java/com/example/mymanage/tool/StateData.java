package com.example.mymanage.tool;

import com.example.mymanage.http.MyToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StateData {
    private static StateData stateData;

    public StateData() {
        log.info("读取配置文件");
        StateData.stateData = this;
    }

    @Value("${times.user-token-effective-time}")
    private long TimeInterval; //30天过期
    @Value("${times.saveDB-time-modify}")
    private int IntervalNumForSaveDB;//秒，倒计时,到时后执行保存数据库
    @Value("${times.verificationCode-effective-time}")
    private int verificationCodeEffectiveTime;//秒，倒计时,到时后执行保存数据库
    @Value("${spring.jackson.date-format}")
    private String dateFormat;

    @Value("${wexin.access-token}")
    private String accessToken;
    @Value("${wexin.appid}")
    private String appid;
    @Value("${wexin.appSecret}")
    private String appSecret;
    @Value("${wexin.envid}")
    private String envId;

    @Value("${wexin.DBQueryUrl}")
    private String DBQueryUrl;
    @Value("${wexin.DBDeleteTableUrl}")
    private String DBDeleteTableUrl;
    @Value("${wexin.DBAddTableUrl}")
    private String DBAddTableUrl;
    @Value("${wexin.DBAddDBUrl}")
    private String DBAddDBUrl;
    @Value("${wexin.UPLoadFileUrl}")
    private String UPLoadFileUrl;
    @Value("${wexin.OpenID}")
    private String OpenID;

    @Value("${wexin.upLoadFileNameFormat}")
    private String upLoadFileNameFormat;

    public static String getUpLoadFileNameFormat() {
        return stateData.upLoadFileNameFormat;
    }

    public static String getOpenID() {
        return stateData.OpenID;
    }

    public static String getDBQueryUrl() {
        return stateData.DBQueryUrl;
    }

    public static String getDBDeleteTableUrl() {
        return stateData.DBDeleteTableUrl;
    }

    public static String getDBAddTableUrl() {
        return stateData.DBAddTableUrl;
    }

    public static String getDBAddDBUrl() {
        return stateData.DBAddDBUrl;
    }

    public static String getUPLoadFileUrl() {
        return stateData.UPLoadFileUrl;
    }


    /**
     * 用户的Token默认有效时间30天
     */
    public static long getUserTokenEffectiveTime() {
        return stateData.TimeInterval;
    }

    /**
     * 默认修改数据5分钟后，将数据保存到远程数据库
     */
    public static int getIntervalNumForSaveDB() {
        return stateData.IntervalNumForSaveDB;
    }

    public static int getVerificationCodeEffectiveTime() {
        return stateData.verificationCodeEffectiveTime;
    }

    public static String getAccessToken() {
        return stateData.accessToken;
    }

    public static String getWXAppId() {
        return stateData.appid;
    }

    public static String getWXAppSecret() {
        return stateData.appSecret;
    }

    public static String getWXEnvId() {
        return stateData.envId;
    }

    public static String getDateFormat() {
        return stateData.dateFormat;
    }

}
