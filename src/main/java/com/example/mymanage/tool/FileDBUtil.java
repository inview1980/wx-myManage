package com.example.mymanage.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FileDBUtil {
    private static final String PATH = "src/main/resources/db/";

    public static <T> List<T> getListFromDB(@NonNull Class<T> tClass) {
        File file = new File(PATH + tClass.getSimpleName());
        log.info("读取数据库文件{}",tClass.getSimpleName());
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String tmpStr = reader.lines().collect(Collectors.joining());
            List<T> collection = JSON.parseArray(tmpStr, tClass);
            if (collection == null) {
                collection = new ArrayList<>();
            }
            return collection;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return new ArrayList<>();
        }
    }


    /**
     * 将集合全部写入云数据库
     *
     * @param tList
     * @return
     */
    public static <T> boolean writeToDB(@NonNull List<T> tList) {
        if(tList.size()==0) return true;
        File file = new File(PATH + tList.get(0).getClass().getSimpleName());
        String tmpStr = JSONArray.toJSONString(tList);
        try(BufferedOutputStream os=new BufferedOutputStream(new FileOutputStream(file))){
            os.write(tmpStr.getBytes());
        }  catch (IOException e) {
            log.error(e.getLocalizedMessage());
            return false;
        }
        log.info("写数据库文件{}成功",tList.get(0).getClass().getSimpleName());
        return true;
    }
}
