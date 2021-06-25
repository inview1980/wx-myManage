package com.example.mymanage.http;

import java.util.Optional;
import java.util.Vector;

public class TokenUtil {
    private static final Vector<MyToken> tokenList = new Vector<>();

    /**
     * 检查Token是否过期，过期删除
     */
    public static void checkTokenOverdue() {
        tokenList.removeIf(MyToken::isOverdue);
    }

    /**
     * 检查Token是否一致
     */
    public static Optional<MyToken> checkToken(String token) {
        return tokenList.stream().filter(t -> t.checkToken(token)).findFirst();
    }

    public static MyToken addToken() {
        MyToken token=new MyToken();
        tokenList.add(token);
        return token;
    }
}
