package com.example.mymanage.tool;

public class StaticConfigData {
    public static final String DateFormatString = "yyyy-M-d";
    /**
     * 用户的Token默认有效时间30天
     */
    public static final long UserTokenEffectiveTime = 30L * 1000L * 60L * 60L * 24L;
    /**
     * 秒，倒计时,到时后执行保存数据库
     * 默认修改数据5分钟后，将数据保存到远程数据库
     */
    public static final int IntervalNumForSaveDB = 60 * 5;
    /**
     * 指定时间后，判断验证码是否过期，默认90秒
     */
    public static final int VerificationCodeEffectiveTime = 1000 * 90;
    public static final String AccessTokenURL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential" +
            "&appid=";
    public static final String AppID = "wx3d5a88359f0a4d13";
    public static final String AppSecret = "4bcf637bba2eb7cd310555187acca3ce";
    public static final String EnvID = "java-yun-3gpjz81xc9b6d908";
    public static final String DBQueryUrl = "https://api.weixin.qq.com/tcb/databasequery?access_token=";
    public static final String DBDeleteTableUrl = "https://api.weixin.qq.com/tcb/databasecollectiondelete?access_token=";
    public static final String DBAddTableUrl = "https://api.weixin.qq.com/tcb/databasecollectionadd?access_token=";
    public static final String DBAddDBUrl = "https://api.weixin.qq.com/tcb/databaseadd?access_token=";
    public static final String UPLoadFileUrl = "https://api.weixin.qq.com/tcb/uploadfile?access_token=";
    public static final String OpenIDUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=";
    public static final String UpLoadFileNameFormatString = "yyyy-MM-dd(HH-mm-ss)";
}
