package com.example.mymanage.tool;

import com.example.mymanage.MainApplication;
import com.example.mymanage.db.MyUserHttp;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.file.StandardCopyOption;

import static org.junit.Assert.assertTrue;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class ReadExcelTest extends TestCase {
    @Autowired
    ReadExcel readExcel;
    @Autowired
    MyUserHttp myUserHttp;


    /**
     * 将远程数据库下载到本地xls文件中
     */
    @Test
    public void saveDB2File() throws IOException, CloneNotSupportedException {
        InputStream is = readExcel.saveDB2File();
        File file = new File("f:\\db.xlsx");
        java.nio.file.Files.copy(
                is,
                file.toPath(),
                StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 将本地xls文件数据上传到远程数据库
     */
    @Test
    public void readXlsToDB() throws IOException {
        String path = "f:\\db.xlsx";
        readExcel.readXlsToDB(path);
    }

    @Test
    public void readXls() throws IOException {
        readExcel.readXls();
        val lst = myUserHttp.getAllList();
    }


}