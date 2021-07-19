package com.example.mymanage.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.mymanage.iface.IReadAndWriteDB;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class FileDBUtil implements IReadAndWriteDB {
    private static final String PATH = "db/";
    private static final String Extension = ".dbTxt";

    @Override
    public  <T> List<T> getListFromDB(@NonNull Class<T> tClass) {
        try {
            String resource = ResourceUtils.getURL("classpath:").getPath();
            resource += tClass.getSimpleName() + Extension;
            log.info("读取数据库文件{}", resource);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(resource)), "UTF-8"));
            String tmpStr = reader.lines().collect(Collectors.joining());
            List<T> collection = JSON.parseArray(tmpStr, tClass);
            reader.close();
            return collection == null ? new ArrayList<>() : collection;
        } catch (IOException e) {
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
    @Override
    public  <T> boolean writeToDB(@NonNull List<T> tList) {
        if (tList.size() == 0) return true;
        try {
            String resource = ResourceUtils.getURL("classpath:").getPath();
            resource += tList.get(0).getClass().getSimpleName() + Extension;
            String tmpStr = JSONArray.toJSONString(tList);
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(new File(resource)));
            os.write(tmpStr.getBytes("UTF-8"));
            os.flush();
            os.close();
            log.info("写数据库文件{}成功", resource);
            return true;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            return false;
        }
    }
}
