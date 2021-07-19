package com.example.mymanage.tool;

import com.alibaba.excel.EasyExcel;
import com.example.mymanage.MainApplication;
import com.example.mymanage.db.MyUserHttp;
import com.example.mymanage.pojo.RoomDetails;
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
import java.util.ArrayList;
import java.util.List;

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
        InputStream is = readExcel.saveDB2FileByEasyExcel();
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
        String path = "f:\\db1.xlsx";
        readExcel.readXlsToDB(path);
    }

    @Test
    public void readXls() throws IOException {
        readExcel.readXls("f:\\db.xlsx");
        val lst = myUserHttp.getAllList();
    }

    @Test
    public void writeExcel(){
        List<RoomDetails> rdLst = new ArrayList<>();
        RoomDetails rd=new RoomDetails();
        rd.setRoomNumber("333");
        rd.setDelete(true);
        rdLst.add(rd);
        RoomDetails rd2=new RoomDetails();
        rd2.setRoomNumber("4444");
        rdLst.add(rd2);
        EasyExcel.write("f:/tmp.xls").sheet("tt").doWrite(rdLst);
    }
//    @Test
//    public void zipFiles() throws IOException {
//        val is = readExcel.zipFiles();
//        FileOutputStream fos = new FileOutputStream("f:/b.zip");
//        byte[] b = new byte[1024];
//        int length;
//        while ((length = is.read(b)) > 0){
//            fos.write(b, 0, length);
//        }
//        is.close();
//        fos.close();
//    }
}