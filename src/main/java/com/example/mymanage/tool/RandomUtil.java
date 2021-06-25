package com.example.mymanage.tool;

import java.util.Random;

public class RandomUtil {
    private static  int defaultByteLength=8;
    private static Random random=new Random();

    /**
     * 生成随机字符串，12位
     *
     * @return
     */
    public static String getString() {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(long2Bytes(defaultByteLength));
    }

    public static String getString(int length) {
        if(length>=6 &&length<12){
            return org.apache.commons.codec.binary.Base64.encodeBase64String(long2Bytes(defaultByteLength)).substring(0,length);
        }
        if(length<6 ) {
            return org.apache.commons.codec.binary.Base64.encodeBase64String(long2Bytes(defaultByteLength)).substring(0,6);
        }
        if(length<24){
            return org.apache.commons.codec.binary.Base64.encodeBase64String(long2Bytes(16)).substring(0,length);
        }
        return org.apache.commons.codec.binary.Base64.encodeBase64String(long2Bytes(24));
    }

    private static byte[] long2Bytes(int length) {
        byte[] byteNum = new byte[length];
        for (int ix = 0; ix < length; ix++) {
            byteNum[ix] = (byte) (random.nextInt(255)-128);
        }
        return byteNum;
    }
}
