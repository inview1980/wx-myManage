package com.example.mymanage.db;

import com.alibaba.fastjson.JSON;
import com.example.mymanage.AppConfig;
import com.example.mymanage.http.HttpResultEnum;
import com.example.mymanage.iface.IGetAllList;
import com.example.mymanage.iface.IWriteToDB;
import com.example.mymanage.pojo.MyUser;
import com.example.mymanage.tool.*;
import lombok.NonNull;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MyUserHttp implements IWriteToDB, IGetAllList<MyUser> {
    private static List<MyUser> myUserList;

    @Synchronized
    @Override
    public List<MyUser> getAllList() {
        if (myUserList == null) {
            try {
                val tmpLst = AppConfig.getiReadAndWriteDB().getListFromDB(MyUser.class);
                for (MyUser user : tmpLst) {
                    String decode = EncryptUtil.decode(user.getPassword(), user.getKey());
                    user.setPassword(decode);
                }
                myUserList = tmpLst;
            } catch (Exception e) {
                log.error(e.toString());
                throw new MyException(HttpResultEnum.DecryptError);
            }
        }
        return myUserList;
    }

    @Override
    public boolean writeToDB() {
        List<MyUser> tmpLst = new ArrayList<>();
        for (MyUser user : getAllList()) {
            MyUser newUser = new MyUser(user.get_id(), user.getUserName(), "", "");
            String key = RandomUtil.getString();
            newUser.setPassword(EncryptUtil.encode(user.getPassword(), key));
            newUser.setKey(key);
            tmpLst.add(newUser);
        }
        return AppConfig.getiReadAndWriteDB().writeToDB(tmpLst);
    }

    @Override
    public void removeDB() {
        myUserList = null;
        log.info("数据已清空");
    }

    public boolean checkUserPassword(@NonNull String username, @NonNull String password) {
        if (StaticConfigData.getDebugState().isDebug()) {
            log.info(JSON.toJSONString(getAllList().stream().collect(Collectors.toMap(MyUser::getUserName,MyUser::getPassword))));
            log.info("用户名：{},密码：{}", username, password);
        }
        return getAllList().stream().anyMatch(user -> username.equals(user.getUserName()) && password.equals(user.getPassword()));
    }

    @Synchronized
    public boolean modifyPassword(String userName, String newPwd) {
        for (MyUser myUser : getAllList()) {
            if (userName.equals(myUser.getUserName())) {
                myUser.setPassword(newPwd);
                TimedTask.SetTableChanged(DBChangeSignEnum.MyUser);
                log.info("用户：{} 密码已更改", userName);
                return true;
            }
        }
        throw new MyException(HttpResultEnum.UserNotExist);
    }
}
