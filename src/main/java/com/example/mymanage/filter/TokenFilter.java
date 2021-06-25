package com.example.mymanage.filter;

import com.example.mymanage.http.HttpResultEnum;
import com.example.mymanage.http.TokenUtil;
import com.example.mymanage.tool.MyException;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Log
public class TokenFilter implements HandlerInterceptor {

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userToken = request.getHeader("userToken");// 获取token
        if (userToken != null && TokenUtil.checkToken(userToken).isPresent()) {
            return true;//如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
        } else {
            throw new MyException(HttpResultEnum.NeedLogin);
        }
    }
}
