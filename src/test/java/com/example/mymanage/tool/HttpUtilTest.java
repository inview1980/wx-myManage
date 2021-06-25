package com.example.mymanage.tool;

import com.example.mymanage.MainApplication;
import com.example.mymanage.http.HttpUtil;
import com.example.mymanage.pojo.PersonDetails;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = MainApplication.class)
public class HttpUtilTest extends TestCase {

    @Test
    public void getAccessToken() {
        HttpUtil.getAccessToken();
        assertNotNull(HttpUtil.getAccessToken());
        log.info(HttpUtil.getAccessToken());
    }

    @Test
    public void writeToDB() {
        List<PersonDetails> dList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            PersonDetails pd=new PersonDetails("name"+i);
            pd.setCompany("公司" + i);
            pd.set_id(i);
            pd.setCard("编码"+i);
            dList.add(pd);
        }
        boolean isOk = HttpUtil.writeToDB(dList, "person-details");
        assertTrue(isOk);
    }
}