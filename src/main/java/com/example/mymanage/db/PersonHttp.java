package com.example.mymanage.db;

import com.example.mymanage.AppConfig;
import com.example.mymanage.http.Result;
import com.example.mymanage.iface.IPersonDB;
import com.example.mymanage.iface.IWriteToDB;
import com.example.mymanage.pojo.PersonDetails;
import com.example.mymanage.tool.TimedTask;
import lombok.NonNull;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j@Component
public class PersonHttp implements IPersonDB, IWriteToDB {
    private static List<PersonDetails> personDetailsList;

    @Override@Synchronized
    public List<PersonDetails> getAllList() {
        if (personDetailsList == null) {
            personDetailsList = AppConfig.getiReadAndWriteDB().getListFromDB(PersonDetails.class);
        }
        return personDetailsList;
    }

    @Override
    public Result addPerson(@NonNull PersonDetails person) {
        if(person.getName()==null||person.getName().equals("")){
            return Result.Error(100001, "租户名不能为空");
        }
        if(getAllList().stream().anyMatch(pe->pe.getName().equals(person.getName())&&
                pe.getTel().equals(person.getTel()))){
            return Result.Error(10000, "租户名重复");
        }
        int maxId= getAllList().stream().mapToInt(PersonDetails::get_id).max().orElse(1);
        person.set_id(++maxId);
        synchronized (object){
            getAllList().add(person);
            TimedTask.SetTableChanged(DBChangeSignEnum.PersonSign);
        }
        log.info("增加租户:" + person.getName());

        return Result.Ok();
    }

    @Override
    public boolean update(@NonNull PersonDetails person) {
        synchronized (object){
            getAllList().removeIf(pd->pd.get_id()==person.get_id());
            getAllList().add(person);
            TimedTask.SetTableChanged(DBChangeSignEnum.PersonSign);
            log.info("修改租户:" + person.getName());
        }
        return true;
    }

    @Override
    public PersonDetails getPersonByID(int manID) {
        synchronized (object){
            return getAllList().stream().filter(p->p.get_id()==manID).findFirst().get();
        }
    }

    @Override
    public void delete(int id) {
        synchronized (object){
            getAllList().removeIf(pd->pd.get_id()==id);
            TimedTask.SetTableChanged(DBChangeSignEnum.PersonSign);
            log.info("删除租户:");
        }
    }


    @Override
    public boolean writeToDB() {
        return AppConfig.getiReadAndWriteDB().writeToDB(personDetailsList);//HttpUtil.writeToDB(personDetailsList, TableName);
    }

    @Override
    public void removeDB() {
        personDetailsList=null;        log.info("数据已清空");

    }

}
