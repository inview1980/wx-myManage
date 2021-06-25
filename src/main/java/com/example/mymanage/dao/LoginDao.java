package com.example.mymanage.dao;


import com.example.mymanage.db.MyUserHttp;
import com.example.mymanage.http.*;
import com.example.mymanage.tool.EncryptUtil;
import com.example.mymanage.tool.MyException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginDao {
    private final MyUserHttp myUserHttp;

    /**
     * 使用verificationCode从后台获取相对应的密码解密pwd,再验证用户名和密码
     *
     * @param userName
     * @param pwd
     * @param verificationCode
     * @return
     * @throws IOException
     */
    public MyToken login(@NonNull String userName, @NonNull String pwd, @NonNull String verificationCode) {
        String key = VerificationCodeUtil.getKey(verificationCode);
        if (key == null) {
            throw new MyException(HttpResultEnum.VerificationCodeEndTime);
        }
        String password = EncryptUtil.decode(pwd, key);
        VerificationCodeUtil.deleteKey(verificationCode);
        boolean isOk = myUserHttp.checkUserPassword(userName, password);
        if (isOk) {
            return TokenUtil.addToken();
        } else {
            throw new MyException(HttpResultEnum.PasswordError);
        }
    }


}
