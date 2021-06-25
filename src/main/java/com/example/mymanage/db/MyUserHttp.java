package com.example.mymanage.db;

import com.example.mymanage.http.HttpResultEnum;
import com.example.mymanage.http.HttpUtil;
import com.example.mymanage.iface.IGetAllList;
import com.example.mymanage.iface.IWriteToDB;
import com.example.mymanage.pojo.MyUser;
import com.example.mymanage.tool.EncryptUtil;
import com.example.mymanage.tool.MyException;
import com.example.mymanage.tool.RandomUtil;
import com.example.mymanage.tool.TimedTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MyUserHttp implements IWriteToDB, IGetAllList<MyUser> {
    private final String TableName = "My-User";
    private static List<MyUser> myUserList;

    @Synchronized
    @Override
    public List<MyUser> getAllList() {
        if (myUserList == null) {
            try {
                val tmpLst = HttpUtil.getListFromDB(MyUser.class, TableName);
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
        return HttpUtil.writeToDB(tmpLst, TableName);
    }

    @Override
    public void removeDB() {
        myUserList = null;
        log.info("数据已清空");

    }

    public boolean checkUserPassword(@NonNull String username, @NonNull String password) {
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
