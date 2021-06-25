package com.example.mymanage.dao;

import com.example.mymanage.MainApplication;
import com.example.mymanage.http.HttpUtil;
import com.example.mymanage.tool.ReadExcel;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class TimedTestTask extends TestCase {
    @Autowired ReadExcel excel;

    @Test
    public void updateDB() {
//        HttpUtil.writeToDB(excel.getRecordList(), "rent-Records");
//        HttpUtil.writeToDB(excel.getRoomDetailsList(), "room-details");
//        HttpUtil.writeToDB(excel.getPersonDetailsList(), "person-details");
//        HttpUtil.writeToDB(excel.getPayPropertyList(), "Pay-Property");
    }

    @Test
    public void upLoadFile() throws IOException, CloneNotSupportedException {
//        String fileId=HttpUtil.upLoadFile(new FileInputStream("F:/db1.xlsx"));
        String fileId=HttpUtil.upLoadFile(excel.saveDB2File());
        assertNotNull(fileId);
        log.info(fileId);
    }
}