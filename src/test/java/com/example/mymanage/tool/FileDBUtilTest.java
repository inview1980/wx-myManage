package com.example.mymanage.tool;

import com.alibaba.excel.EasyExcel;
import com.example.mymanage.MainApplication;
import com.example.mymanage.db.DBChangeSignEnum;
import com.example.mymanage.iface.IGetAllList;
import com.example.mymanage.iface.IReadAndWriteDB;
import com.example.mymanage.pojo.MyUser;
import com.example.mymanage.pojo.RoomDetails;
import junit.framework.TestCase;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class FileDBUtilTest extends TestCase {
    @Autowired
    private ApplicationContext context;
    @Value("${resource.default-db-file}")
    private String path;
    @Autowired private IReadAndWriteDB iReadAndWriteDB;

    @Test
    public void getListFromDB() {
        val lst = iReadAndWriteDB.getListFromDB(MyUser.class);
        assertNotNull(lst);
        assertTrue(lst.size() > 0);
        System.out.println(lst.get(0));
    }

    @Test
    public void writeToDB() {
        List<MyUser> users = new ArrayList<>();
        users.add(new MyUser(1, "inview", "", ""));
        users.add(new MyUser(2, "22222", "222", "3333"));
        iReadAndWriteDB.writeToDB(users);
    }

    @Test
    public void readFileToFileDB() {
        for (DBChangeSignEnum value : DBChangeSignEnum.values()) {
            List<?> allList = ((IGetAllList<?>) context.getBean(value.getBeanClass())).getAllList();
            allList.clear();
            EasyExcel.read(path, value.getPojoClass(), new ReadExcel.MyExcelListener(allList)).sheet(value.getPojoClass().getSimpleName()).doReadSync();
            iReadAndWriteDB.writeToDB(allList);
        }
    }

    @Test
    public void test() throws FileNotFoundException {
        System.out.println(ClassUtils.getDefaultClassLoader().getResource("").getPath());
        System.out.println(ResourceUtils.getURL("classpath:").getPath());
    }
}