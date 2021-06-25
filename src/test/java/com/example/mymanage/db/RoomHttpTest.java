package com.example.mymanage.db;

import com.example.mymanage.MainApplication;
import com.example.mymanage.iface.IRoomDB;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = MainApplication.class)
public class RoomHttpTest extends TestCase {
    @Resource
    private IRoomDB iRoomDB;

    @Test
    public void getRoomDetailsList() {
        val lst = iRoomDB.getAllList();
        assertTrue(lst.size() > 2);
        log.info(lst.get(1).toString());
        log.info(lst.get(3).toString());
        log.info(lst.get(5).toString());
    }
}