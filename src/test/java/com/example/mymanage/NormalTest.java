package com.example.mymanage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.mymanage.http.HttpResultEnum;
import com.example.mymanage.http.HttpUtil;
import com.example.mymanage.http.MyToken;
import com.example.mymanage.pojo.MyUser;
import com.example.mymanage.tool.MyException;
import com.example.mymanage.tool.StaticConfigData;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@Slf4j
public class NormalTest {
    @Test
    public void test() throws IOException {
//        SimpleDateFormat format = new SimpleDateFormat("(yyyy-MM-dd HH-mm-ss)");
//        String tmpFilePath ="src/main/resources/tmp" +  format.format(new Date())+ ".xls";
//        log.info(tmpFilePath);

        File file=new File("F:\\program\\微信小程序\\myManage\\miniprogram" +
                "\\images\\pic.jpg");
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
        FileOutputStream os = new FileOutputStream("f:\\pic.txt");
        byte[] data = new byte[(int) file.length()];
        is.read(data);
        is.close();
        String tmp = Base64.getEncoder().encodeToString(data);
        os.write(tmp.getBytes());
        os.flush();
        os.close();
    }

    @Test
    public void test1() throws CloneNotSupportedException {
        MyUser user = new MyUser(1, "1111", "2222", "3333");
        MyUser u2= (MyUser) user.clone();
        u2.setPassword("00000");
        System.out.println(user);
        System.out.println(u2);
    }

    @Test
    public void test2() throws URISyntaxException {
        String url1 = StaticConfigData.AccessTokenURL;
        url1 += StaticConfigData.AppID;
        url1 += "&secret=";
        url1 += StaticConfigData.AppSecret;

        HttpGet post = new HttpGet(url1);

        URI uri = post.getURI();
        String url=uri.toString();
        System.out.println(uri.getQuery());
//        String s1=url.substring(0, url.indexOf("=")+1)+"new Access";
//        System.out.println(s1);
//        post.setURI(new URI(s1));
//        System.out.println(post.getURI().toString());
    }

    @Test
    public void file() throws IOException {
        File path= File.createTempFile("updataFile",".xlsx" );
        System.out.println(path.getAbsolutePath());
        System.out.println(path.getPath());
    }
}
