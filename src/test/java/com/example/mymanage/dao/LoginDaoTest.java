package com.example.mymanage.dao;

import com.example.mymanage.tool.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.bytebuddy.utility.RandomString;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@Slf4j
public class LoginDaoTest {

    @Test
    public void writeConfig() throws IOException {
        Map<String, String> map = new HashMap<>();
        String password = "kkYYkk1122";
        String key=RandomString.make();
        map.put("password", EncryptUtil.encode(password,key));
        map.put("key", key);
//        assertTrue(LoginDao.writeConfig(map));
    }

    @Test
    public void readConfig() throws Exception {
//        val tt = LoginDao.readConfig();
//        log.info(tt.toString());
//        assertNotNull(tt);
//        String pwd = EncryptUtil.decode(tt.get("password"), tt.get("key"));
//        log.info(pwd);
    }

    @Test
    public void login() throws IOException {
//        val tt = LoginDao.login(userName, "kkk", "lkl");
//        log.info(tt);
//        assertNotNull(tt);
    }
}