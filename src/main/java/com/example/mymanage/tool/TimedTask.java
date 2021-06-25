package com.example.mymanage.tool;

import com.example.mymanage.db.DBChangeSignEnum;
import com.example.mymanage.http.HttpUtil;
import com.example.mymanage.http.TokenUtil;
import com.example.mymanage.http.VerificationCodeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统定时任务，主要任务：自动上传数据库的更改、自动更新上传时需要的AccessToken、维护用户登录时的Token
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@EnableAsync
public class TimedTask {
    private static int SaveDBNow = 0;//秒，倒计时
    public static boolean isInit = true;
    private static final Map<Integer, DBChangeSignEnum> signMap = new HashMap<Integer, DBChangeSignEnum>() {{
        for (DBChangeSignEnum value : DBChangeSignEnum.values()) {
            put(value.ordinal(), value);
        }
    }};

    private static final Object object = new Object();

    /**
     * 设置相对应的数据库中的表为已更改状态，以便随后更新远程数据库
     */
    public static void SetTableChanged(DBChangeSignEnum signEnum){
        signMap.get(signEnum.ordinal()).isChanged(object);
        SaveDBNow=0;//如果已标记数据库已更改，将修改远程数据库的计时置0
    }

    //到指定时间后，判断数据库是否改变。如已改变，通知数据库更新程序
    @Scheduled(fixedRate = 1000)
    @Async
    void checkDBModify() {
        //每秒减1，小于0则需要将数据重写入远程数据库,默认300秒后
        if (SaveDBNow >= StateData.getIntervalNumForSaveDB()) {
            synchronized (object) {
                for (DBChangeSignEnum entry : signMap.values()) {
                    entry.checkChanged();
                }
            }
            SaveDBNow = 0;
        } else {
            SaveDBNow++;
        }
    }

    //间隔1小时，检查发送给用户的Token是否过期
    @Scheduled(fixedRate = 1000 * 60*60)
    void checkTokenOverdue() {
        TokenUtil.checkTokenOverdue();
    }

    //到规定间隔时间，更新AccessToken,因为远程服务器默认有效期为7200秒
    @Scheduled(fixedRate = 1000 * 7000)
    @Async
    void checkAccessTokenOverdue() {
        if (!isInit) {
            HttpUtil.getAccessTokenFromHttp();
        }
    }

    /**
     * 每秒检查验证码是否过期
     */
    @Scheduled(fixedRate = 2000)
    void checkVerificationCodeOverdue() {
        VerificationCodeUtil.checkTokenOverdue();
    }
}
