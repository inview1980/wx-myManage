package com.example.mymanage.db;

import com.example.mymanage.MainApplication;
import com.example.mymanage.pojo.MyUser;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = MainApplication.class)
public class MyUserHttpTest extends TestCase {
    @Autowired
    MyUserHttp userHttp;

    @Test
    public void writeToDB()  {
        List<MyUser> userList = userHttp.getAllList();
        userList.clear();
        userList.add(new MyUser(1, "inview", "123456", ""));
//        userList.add(new MyUser.dbTxt(2, "test", "123456", ""));
        assertTrue(userHttp.writeToDB());
    }

    @Test
    public void getMyUserList()  {
        List<MyUser> userList = userHttp.getAllList();
        userList.forEach(user -> log.info(user.toString()));
    }
}