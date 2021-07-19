package com.example.mymanage.db;

import com.example.mymanage.AppConfig;
import com.example.mymanage.iface.IWriteToDB;
import com.example.mymanage.pojo.PayProperty;
import com.example.mymanage.tool.TimedTask;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j@Component
public class PayPropertyHttp implements IWriteToDB, com.example.mymanage.iface.IPayPropertyDB {
    private static List<PayProperty> payPropertyList;
    private final static Object object=new Object();

    @Override@Synchronized
    public List<PayProperty> getAllList() {
        if (payPropertyList == null) {
            synchronized (object){
                payPropertyList = AppConfig.getiReadAndWriteDB().getListFromDB(PayProperty.class);
            }
        }
        return payPropertyList;
    }

    @Override
    public boolean writeToDB() {
        return AppConfig.getiReadAndWriteDB().writeToDB(payPropertyList);//HttpUtil.writeToDB(payPropertyList, TableName);
    }

    @Override
    public void removeDB() {
        payPropertyList=null;
        log.info("数据已清空");

    }

    @Override
    public boolean deletePayPropertyByID(int id) {
        synchronized (object){
            return getAllList().removeIf(pp -> pp.get_id() == id);
        }
    }

    @Override
    public boolean add(PayProperty pp) {
        int maxID = getAllList().stream().mapToInt(PayProperty::get_id).max().orElse(1);
        pp.set_id(++maxID);
        synchronized (object){
            getAllList().add(pp);
            TimedTask.SetTableChanged(DBChangeSignEnum.PayProperty);
        }
        return true;
    }

    @Override
    public boolean update(PayProperty pp) {
        synchronized (object){
            getAllList().removeIf(p1->p1.get_id()==pp.get_id());
            getAllList().add(pp);
        }
        return true;
    }
}
