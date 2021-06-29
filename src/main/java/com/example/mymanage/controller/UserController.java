package com.example.mymanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.mymanage.dao.UserDao;
import com.example.mymanage.db.DBChangeSignEnum;
import com.example.mymanage.http.Result;
import com.example.mymanage.iface.IWriteToDB;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserDao userDao;
    private final ApplicationContext context;

//    @PostMapping("/getOpenId")
//    Result getUserInfo(@RequestBody JSONObject jsonParam) throws Exception {
////        log.info("jsonParam:" + jsonParam);
//        String code = jsonParam.get("code").toString();
//
//        return Result.Ok(HttpUtil.getOpenID(code));
//    }

    @PostMapping("/modifyPwd")
    Result modifyPassword(@RequestBody JSONObject jsonObject) {
        String userName = jsonObject.getString("username");
        String oldPassword = jsonObject.getString("oldPassword");
        String newPassword = jsonObject.getString("newPassword");
        String verificationCode = jsonObject.getString("VerificationCode");
        return Result.Ok(userDao.modifyPassword(userName, oldPassword, newPassword, verificationCode));
    }

    @PostMapping("/reloadDB")
    Result reloadDB() {
        for (DBChangeSignEnum value : DBChangeSignEnum.values()) {
            ((IWriteToDB) context.getBean(value.getBeanClass())).removeDB();
        }
        return Result.Ok();
    }

    @PostMapping("/reloadDBByNonVerify")
    Result reloadDBByNonVerify() {
        for (DBChangeSignEnum value : DBChangeSignEnum.values()) {
            ((IWriteToDB) context.getBean(value.getBeanClass())).removeDB();
        }
//        HttpUtil.setAccessToken();
        return Result.Ok();
    }
}
