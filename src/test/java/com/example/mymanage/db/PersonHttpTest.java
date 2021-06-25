package com.example.mymanage.db;

import com.alibaba.fastjson.JSONArray;
import com.example.mymanage.MainApplication;
import com.example.mymanage.iface.IPersonDB;
import com.example.mymanage.pojo.PersonDetails;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = MainApplication.class)
public class PersonHttpTest extends TestCase {
    private IPersonDB iPersonDB = new PersonHttp();

    @Test
    public void getPersonDetailsList() {
        val lst = iPersonDB.getAllList();
        assertTrue(lst.size() > 2);
        log.info(lst.get(1).toString());
    }

    @Test
    public void getPersonDetailsList2() {
        String tmp = "[\"{\\\"_id\\\":1,\\\"company\\\":\\\"优信\\\",\\\"cord\\\":null,\\\"name\\\":\\\"钱萍\\\",\\\"other\\\":\\\"微信号：wxid_fw4vetzzorsb21,另：夏琪：15927172454\\\",\\\"tel\\\":null}\",\"{\\\"_id\\\":2,\\\"company\\\":\\\"好租宝\\\",\\\"cord\\\":\\\"\\\",\\\"name\\\":\\\"昌苗\\\",\\\"other\\\":\\\"\\\",\\\"tel\\\":\\\"13638662856\\\"}\",\"{\\\"_id\\\":3,\\\"company\\\":\\\"悦然装饰\\\",\\\"cord\\\":\\\"420529198711253042\\\",\\\"name\\\":\\\"刘宾娇\\\",\\\"other\\\":null,\\\"tel\\\":\\\"15926460368\\\"}\",\"{\\\"_id\\\":4,\\\"company\\\":null,\\\"cord\\\":null,\\\"name\\\":\\\"未知1\\\",\\\"other\\\":null,\\\"tel\\\":null}\",\"{\\\"_id\\\":5,\\\"company\\\":null,\\\"cord\\\":null,\\\"name\\\":\\\"未知2\\\",\\\"other\\\":null,\\\"tel\\\":null}\",\"{\\\"_id\\\":6,\\\"company\\\":null,\\\"cord\\\":null,\\\"name\\\":\\\"未知3\\\",\\\"other\\\":null,\\\"tel\\\":null}\",\"{\\\"_id\\\":7,\\\"company\\\":null,\\\"cord\\\":null,\\\"name\\\":\\\"未知3\\\",\\\"other\\\":null,\\\"tel\\\":null}\",\"{\\\"_id\\\":8,\\\"company\\\":\\\"未知\\\",\\\"cord\\\":null,\\\"name\\\":\\\"未知\\\",\\\"other\\\":null,\\\"tel\\\":null}\"]";
        String tmp2 = "[{\"name\":\"jjj\",\"primary_id\":0},{\"name\":\"jjj2\",\"primary_id\":0},{\"name\":\"jjj3\",\"primary_id\":0}]";
        tmp = tmp.replace("[\"{", "[{").replace("}\"]", "}]");
        tmp = tmp.replace("\\", "").replace("}\",\"{", "},{");
        log.info(tmp);
        val tt = JSONArray.parseArray(tmp,PersonDetails.class);
        log.info( tt.get(2).toString());
    }


}