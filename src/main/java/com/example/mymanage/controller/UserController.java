package com.example.mymanage.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.example.mymanage.dao.LoginDao;
import com.example.mymanage.dao.UserDao;
import com.example.mymanage.db.DBChangeSignEnum;
import com.example.mymanage.http.HttpUtil;
import com.example.mymanage.http.Result;
import com.example.mymanage.http.VerificationCodeUtil;
import com.example.mymanage.iface.IGetAllList;
import com.example.mymanage.iface.IWriteToDB;
import com.example.mymanage.pojo.MyUser;
import com.example.mymanage.tool.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserDao userDao;
    private final ApplicationContext context;

    @PostMapping("/getOpenId")
    Result getUserInfo(@RequestBody JSONObject jsonParam) throws Exception {
//        log.info("jsonParam:" + jsonParam);
        String code = jsonParam.get("code").toString();

        return Result.Ok(HttpUtil.getOpenID(code));
    }

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
        return Result.Ok();
    }
}
