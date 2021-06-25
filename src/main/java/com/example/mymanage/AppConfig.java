package com.example.mymanage;

import com.example.mymanage.db.PayPropertyHttp;
import com.example.mymanage.db.PersonHttp;
import com.example.mymanage.db.RentRecordHttp;
import com.example.mymanage.db.RoomHttp;
import com.example.mymanage.filter.TokenFilter;
import com.example.mymanage.iface.IPayPropertyDB;
import com.example.mymanage.iface.IPersonDB;
import com.example.mymanage.iface.IRentRecordDB;
import com.example.mymanage.iface.IRoomDB;
import com.example.mymanage.tool.StateData;
import com.example.mymanage.tool.ReadExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Autowired private StateData appInit;//默认初始化类

    @Bean
    public IRentRecordDB iRentRecordDB() {
//        return new ReadExcel();
        return new RentRecordHttp();
    }

    @Bean
    public IRoomDB iRoomDB() {
//        return new ReadExcel();
        return new RoomHttp();
    }

    @Bean
    public IPersonDB iPersonDB() {
//        return new ReadExcel();
        return new PersonHttp();
    }

    @Bean
    public IPayPropertyDB payProperty() {
//        return new ReadExcel();
        return new PayPropertyHttp();
    }

    @Bean
    public ReadExcel ReadExcel() {
        return new ReadExcel();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new TokenFilter());
        registration.addPathPatterns("/**");                      //所有路径都被拦截
        registration.excludePathPatterns(                         //添加不拦截路径
                "/login/login",            //登录
//                "/**/*.html",            //html静态资源
//                "/**/*.js",              //js静态资源
//                "/**/*.css",             //css静态资源
//                "/**/*.woff",
//                "/**/*.ttf"
                "/login/getVerificationCode",
                "/user/reloadDBByNonVerify"
        );
    }

//    @PostConstruct
//    public void init(){
//        appInit.init(appInit);
//    }
}
