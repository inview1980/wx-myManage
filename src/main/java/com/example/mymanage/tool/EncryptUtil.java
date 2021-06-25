package com.example.mymanage.tool;

import com.example.mymanage.http.HttpResultEnum;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;

@Slf4j
public class EncryptUtil {
    private final static String ALGORITHM_DES = "DES/ECB/PKCS5Padding";

//    static {
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//    }


    //获取加密或解密的Cipher对象：负责完成加密或解密工作
    private static Cipher GetCipher(int opmode, String key) {
        try {
            //根据传入的秘钥内容生成符合DES加密解密格式的秘钥内容
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            //获取DES秘钥生成器对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // 生成秘钥：key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            //获取DES/ECB/PKCS7Padding该种级别的加解密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            //初始化加解密对象【opmode:确定是加密还是解密模式；secretKey是加密解密所用秘钥】
            cipher.init(opmode, secretKey);
            return cipher;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * DES算法，加密
     *
     * @param data 待加密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws InvalidAlgorithmParameterException
     * @throws Exception
     */
    public static String encode(@NonNull String data,@NonNull String key) {
        if (data == null || data.isEmpty())
            return null;
        try {
            //获取加密对象【Cipher.ENCRYPT_MODE：指定加密模式为1】
            Cipher cipher = GetCipher(Cipher.ENCRYPT_MODE, key);
            if (cipher == null) {
                return null;
            } else {
                //设置加密的字符串为utf-8模式并且加密，返回加密后的byte数组。
                byte[] byteHex = cipher.doFinal(data.getBytes("UTF-8"));
                return Base64.encodeBase64String(byteHex);//对加密后的数组进制转换
            }
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * DES算法，解密
     *
     * @param data 待解密字符串
     * @param key  解密私钥，长度不能够小于8位
     * @return 解密后的字节数组
     * @throws Exception
     * @throws Exception 异常
     */
    public static String decode(@NonNull String data,@NonNull String key)  {
        try {
            //先把待解密的字符串转成Char数组类型，然后进行进制转换。
            byte[] b = Base64.decodeBase64(data);            //获取解密对象【Cipher.DECRYPT_MODE：指定解密模式为2】
            Cipher cipher = GetCipher(Cipher.DECRYPT_MODE, key);
            if (cipher != null)
                //进行解密返回utf-8类型的字符串
                return new String(cipher.doFinal(b), "UTF-8");
            else
                throw new MyException(HttpResultEnum.PasswordError);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return null;
        }
    }
}
