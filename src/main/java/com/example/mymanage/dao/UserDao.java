package com.example.mymanage.dao;

import com.example.mymanage.db.MyUserHttp;
import com.example.mymanage.http.HttpResultEnum;
import com.example.mymanage.http.MyToken;
import com.example.mymanage.db.TokenHttp;
import com.example.mymanage.http.VerificationCodeUtil;
import com.example.mymanage.pojo.MyUser;
import com.example.mymanage.tool.EncryptUtil;
import com.example.mymanage.tool.MyException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDao {
    private final MyUserHttp myUserHttp;

    public MyToken modifyPassword(@NonNull String userName, @NonNull String oldPassword, @NonNull String newPassword, @NonNull String verificationCode) {
        if (myUserHttp.getAllList().stream().map(MyUser::getUserName).noneMatch(userName::equals)) {
            log.error("用户名：{}不存在", userName);
            throw new MyException(HttpResultEnum.UserNotExist);
        }
        String key = VerificationCodeUtil.getKey(verificationCode);
        VerificationCodeUtil.deleteKey(verificationCode);
        if (key == null) {
            throw new MyException(HttpResultEnum.VerificationCodeEndTime);
        }
        String oldPwd = EncryptUtil.decode(oldPassword, key);
        if (!myUserHttp.checkUserPassword(userName, oldPwd)) {
            throw new MyException(HttpResultEnum.PasswordError);
        }
        String newPwd = EncryptUtil.decode(newPassword, key);
        if (myUserHttp.modifyPassword(userName, newPwd)) {
            return TokenHttp.addToken();
        }else {
            throw new MyException(HttpResultEnum.Error);
        }
    }
}
