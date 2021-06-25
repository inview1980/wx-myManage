package com.example.mymanage.http;

import lombok.NonNull;
import lombok.val;

import java.util.Vector;

public class VerificationCodeUtil {
    private static final Vector<VerificationCode> verificationList = new Vector<>();

    /**
     * 检查验证码是否过期，过期删除
     */
    public static void checkTokenOverdue() {
        verificationList.removeIf(VerificationCode::isOverdue);
    }

    public static String getKey(@NonNull String verificationCode) {
        val tmp = verificationList.stream().filter(vc -> verificationCode.equals(vc.getVerificationCode())).findFirst();
        return tmp.map(VerificationCode::getKey).orElse(null);
    }

    public static VerificationCode build(){
        VerificationCode code=new VerificationCode();
        verificationList.add(code);
        return code;
    }

    public static void deleteKey(@NonNull String verificationCode){
        verificationList.removeIf(vc -> verificationCode.equals(vc.getVerificationCode()));
    }
}
