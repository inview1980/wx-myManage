package com.example.mymanage;

import com.alibaba.fastjson.JSON;
import com.example.mymanage.http.MyToken;
import com.example.mymanage.pojo.MyUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;
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
    public void test2(){
        MyToken token=new MyToken();
        String tmp=JSON.toJSONString(token);
        System.out.println(tmp);

        MyToken k2=JSON.parseObject(tmp,MyToken.class);
        System.out.println(k2);
    }
}
