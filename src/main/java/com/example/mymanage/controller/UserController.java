package com.example.mymanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.mymanage.dao.UserDao;
import com.example.mymanage.db.DBChangeSignEnum;
import com.example.mymanage.db.TokenHttp;
import com.example.mymanage.http.Result;
import com.example.mymanage.iface.IWriteToDB;
import com.example.mymanage.pojo.DebugState;
import com.example.mymanage.tool.StaticConfigData;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserDao userDao;

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

    @PostMapping("/getDebugState")
    Result getDebugState(HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        String userToken = request.getHeader("userToken");// 获取token
        map.put ("isLogin",userToken != null && TokenHttp.checkToken(userToken));
        map.put(DebugState.class.getSimpleName(), StaticConfigData.getDebugState());
        return Result.Ok(map);
    }
}
