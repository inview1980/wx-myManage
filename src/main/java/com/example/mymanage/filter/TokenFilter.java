package com.example.mymanage.filter;

import com.alibaba.fastjson.JSON;
import com.example.mymanage.http.HttpResultEnum;
import com.example.mymanage.http.Result;
import com.example.mymanage.db.TokenHttp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class TokenFilter implements HandlerInterceptor {

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
//        return true;
        String userToken = request.getHeader("userToken");// 获取token
        if (userToken != null && TokenHttp.checkToken(userToken)) {
            return true;//如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
        } else {
            ServletOutputStream outputStream = response.getOutputStream();
            String resultStr= JSON.toJSONString(Result.Error(HttpResultEnum.NeedLogin));
            outputStream.write(resultStr.getBytes("UTF-8"));
            log.error("需要登录");
            return false;
//            throw new MyException(HttpResultEnum.NeedLogin);
        }
    }
}
