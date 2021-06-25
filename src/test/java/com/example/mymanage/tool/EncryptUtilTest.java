package com.example.mymanage.tool;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.*;

@Slf4j
public class EncryptUtilTest {


    @Test
    public void encode() throws Exception {
        String key = "1234567890";
        String pwd = EncryptUtil.encode("1233321", key);
        log.info(pwd);
        String des = EncryptUtil.decode(pwd, key);
        log.info(des);
    }

    @Test
    public void decode() throws Exception {
//        String pwd = EncryptUtil.decode("ED5wLgc3Mnw=", "12345678");
//        log.info(pwd);
        String pwd = "AP2bo1VFkJM=";
        String key="wNqCMHCe+Nc=";
        System.out.println(EncryptUtil.decode(pwd,key));
//        Random random=new Random();
//        for (int i = 0; i < 100; i++) {
//            key = RandomUtil.getString(random.nextInt(20)+6);
//            log.info(key);
//        }

    }

}