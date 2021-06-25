package com.example.mymanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.mymanage.dao.LoginDao;
import com.example.mymanage.http.Result;
import com.example.mymanage.http.VerificationCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginDao loginDao;

    @PostMapping("/login")
    Result login(@RequestBody JSONObject jsonObject) {
        String userName = jsonObject.getString("username");
        String pwd = jsonObject.getString("password");
        String verificationCode = jsonObject.getString("VerificationCode");
        return Result.Ok(loginDao.login(userName, pwd, verificationCode));
    }

    @PostMapping("/getVerificationCode")
    Result getVerificationCode(@RequestBody JSONObject jsonObject) {
        String verificationCode = jsonObject.getString("verificationCode");
        if (verificationCode != null) {
            VerificationCodeUtil.deleteKey(verificationCode);
        }
        val code = VerificationCodeUtil.build();
        Map<String, String> map = new HashMap<>();
        map.put("verificationCode", code.getVerificationCode());
        map.put("key", code.getKey());
        return Result.Ok(map);
    }
}
