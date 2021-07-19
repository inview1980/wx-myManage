package com.example.mymanage;

import com.example.mymanage.db.*;
import com.example.mymanage.filter.TokenFilter;
import com.example.mymanage.http.HttpUtil;
import com.example.mymanage.iface.*;
import com.example.mymanage.tool.FileDBUtil;
import com.example.mymanage.tool.StaticConfigData;
import com.example.mymanage.tool.ReadExcel;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Value("server.tomcat.basedir")
    private String tempPath;
    private static IReadAndWriteDB iReadAndWriteDB;

//    @Bean@DependsOn("iReadAndWriteDB")
//    public IRentRecordDB iRentRecordDB() {
//        return new RentRecordHttp();
//    }
//
//    @Bean@DependsOn("iReadAndWriteDB")
//    public MyUserHttp myUserHttp() {
//        return new MyUserHttp();
//    }
//
//    @Bean@DependsOn("iReadAndWriteDB")
//    public IRoomDB iRoomDB() {
//        return new RoomHttp();
//    }
//
//    @Bean@DependsOn("iReadAndWriteDB")
//    public IPersonDB iPersonDB() {
//        return new PersonHttp();
//    }
//
//    @Bean@DependsOn("iReadAndWriteDB")
//    public IPayPropertyDB payProperty() {
//        return new PayPropertyHttp();
//    }

    @Synchronized
    public static IReadAndWriteDB getiReadAndWriteDB() {
        if(AppConfig.iReadAndWriteDB==null){
            AppConfig.iReadAndWriteDB=new HttpUtil();
//            AppConfig.iReadAndWriteDB=new FileDBUtil();
        }
        return AppConfig.iReadAndWriteDB;
    }

//    @Bean
////    @DependsOn("iReadAndWriteDB")
//    public TokenHttp tokenHttp() {
//        this.tokenHttp = new TokenHttp();
//        return tokenHttp;
//    }
//
//    @Bean
//    public ReadExcel ReadExcel() {
//        return new ReadExcel();
//    }


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
                "/user/reloadDBByNonVerify",
                "/user/getDebugState",
                "/user/reReadRemoteDBByNonVerify"
        );
    }

//    @PostConstruct
//    public void init(){
//        appInit.init(appInit);
//    }

    /**
     * 配置springboot临时目录
     *
     * @return
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(tempPath);
        return factory.createMultipartConfig();
    }
}
