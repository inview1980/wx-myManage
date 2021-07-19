package com.example.mymanage.http;

import lombok.Getter;

public enum HttpResultEnum {
    /**
     * 密码错误
     */
    PasswordError(10050, "密码错误"),
    /**
     * 未登录，请先登录
     */
    NeedLogin(10051, "未登录，请先登录"),
    /**
     * 获取数据库AccessToken失败！
     */
    GetAccessTokenError(10052, "获取数据库AccessToken失败！"),
    /**
     * 参数错误
     */
    ParameterError(10053, "参数错误"),
    /**
     * 上传文件失败
     */
    UploadFileError(10054, "上传文件失败"),
    /**
     * POST出错
     */
    POSTError(10055, "POST出错"),
    /**
     * 转换参数出错!
     */
    ParameterChangeError(10056, "转换参数出错!"),
    UserNotExist(10057,"该用户不存在"),

    /**
     * 上传文件失败，上传文件不存在
     */
    UploadFileErrorFileNotExists(10058, "上传文件失败，上传文件不存在"),
    /**
     * 解密密码信息时发生错误
     */
    DecryptError(11000,"解密密码信息时发生错误"),
    Error(10098, "未知错误"),
    IOError(10099, "IO错误"),
    AccessTokenPass(10100,"AccessToken过期或失效，请重新进入页面"),
    /**
     * 验证码过期
     */
    VerificationCodeEndTime(10099, "验证码过期");


    @Getter
    private final int code;
    @Getter
    private final String codeMsg;

    HttpResultEnum(int code, String codeMsg) {
        this.code = code;
        this.codeMsg = codeMsg;
    }
}
