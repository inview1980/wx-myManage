package com.example.mymanage.db;

import com.example.mymanage.http.MyToken;
import com.example.mymanage.iface.IGetAllList;
import com.example.mymanage.iface.IRoomDB;
import com.example.mymanage.iface.IWriteToDB;
import com.example.mymanage.tool.FileDBUtil;
import com.example.mymanage.tool.TimedTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Vector;

@Slf4j@Component
public class TokenHttp  implements IWriteToDB, IGetAllList<MyToken> {
    private static  Vector<MyToken> tokenList;
    private static final Object object=new Object();
    private static TokenHttp tokenHttp;

    public TokenHttp() {
        TokenHttp.tokenHttp=this;
    }

    /**
     * 检查Token是否过期，过期删除
     */
    public static void checkTokenOverdue() {
        tokenHttp.getAllList().removeIf(MyToken::isOverdue);
    }

    /**
     * 检查Token是否一致
     */
    public static Optional<MyToken> checkToken(String token) {
        return tokenHttp.getAllList().stream().filter(t -> t.checkToken(token)).findFirst();
    }

    public static MyToken addToken() {
        MyToken token=new MyToken();
        tokenHttp.getAllList().add(token);
        TimedTask.SetTableChanged(DBChangeSignEnum.MyToken);
        return token;
    }

    @Override
    public boolean writeToDB() {
        return FileDBUtil.writeToDB(tokenList);
    }

    @Override
    public void removeDB() {
        log.info("数据已清空");
        tokenList=null;
    }

    @Override
    public List<MyToken> getAllList() {
        synchronized (object){
            if(tokenList==null){
                tokenList=new Vector<>(FileDBUtil.getListFromDB(MyToken.class));
            }
        }
        return tokenList;
    }
}
