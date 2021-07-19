package com.example.mymanage.tool;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.example.mymanage.db.DBChangeSignEnum;
import com.example.mymanage.http.HttpResultEnum;
import com.example.mymanage.iface.IGetAllList;
import com.example.mymanage.iface.IWriteToDB;
import com.example.mymanage.pojo.MyUser;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ReadExcel {

    @Autowired
    private ApplicationContext context;


    /**
     * 根据DBChangeSignEnum枚举中的值，读取相应的数据并保存到excel文件中
     *
     * @return
     */
    public byte[] saveDB2File() {
        val excel = new ExcelExportUtil();
        for (DBChangeSignEnum value : DBChangeSignEnum.values()) {
            List<?> allList = ((IGetAllList<?>) context.getBean(value.getBeanClass())).getAllList();
            log.info("读取表{}数据，共{}项。", value.getPojoClass().getSimpleName(), allList.size());
            if (value == DBChangeSignEnum.MyUser) {//如果是MyUser表，加密密码后再保存
                excel.add(encodeUserList(allList));
                continue;
            }
            excel.add(allList);
        }
        return excel.build().write();
    }

    /**
     * 将用户信息列表中，密码进行加密
     */
    @SneakyThrows
    private List<MyUser> encodeUserList(List<?> allList) {
        List<MyUser> tmpLst = new ArrayList<>();
        for (Object user : allList) {
            MyUser u = (MyUser) ((MyUser) user).clone();
            u.setPassword(EncryptUtil.encode(u.getPassword(), u.getKey()));
            tmpLst.add(u);
        }
        return tmpLst;
    }

    /**
     * 根据DBChangeSignEnum枚举中的值，读取相应的数据并保存到excel文件中
     */
    public InputStream saveDB2FileByEasyExcel() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        ExcelWriter excelWriter = EasyExcel.write(os).build();
        for (DBChangeSignEnum value : DBChangeSignEnum.values()) {
            List<?> allList = ((IGetAllList<?>) context.getBean(value.getBeanClass())).getAllList();
            if (value == DBChangeSignEnum.MyUser) {//如果是MyUser表，加密密码后再保存
                List<MyUser> tmpLst = encodeUserList(allList);
                String simpleName = value.getPojoClass().getSimpleName();
                excelWriter.write(tmpLst, EasyExcel.writerSheet(simpleName).head(value.getPojoClass()).build());
                continue;
            }
            String simpleName = value.getPojoClass().getSimpleName();
            excelWriter.write(allList, EasyExcel.writerSheet(simpleName).head(value.getPojoClass()).build());
        }
        excelWriter.finish();
        return new ByteArrayInputStream(os.toByteArray());
    }


    public void readXlsToDB(@NonNull String path) {
        for (DBChangeSignEnum value : DBChangeSignEnum.values()) {
            List<?> allList = ((IGetAllList<?>) context.getBean(value.getBeanClass())).getAllList();
            allList.clear();
            EasyExcel.read(path, value.getPojoClass(), new MyExcelListener(allList))
                    .sheet(value.getPojoClass().getSimpleName()).doReadSync();
            if (value == DBChangeSignEnum.MyUser) {
                for (Object user : allList) {
                    MyUser user1 = (MyUser) user;
                    user1.setPassword(EncryptUtil.decode(user1.getPassword(), user1.getKey()));
                }
            }
            ((IWriteToDB) context.getBean(value.getBeanClass())).writeToDB();
        }
    }


    public void readXls(@NonNull String path) {
        for (DBChangeSignEnum value : DBChangeSignEnum.values()) {
            List<?> allList = ((IGetAllList<?>) context.getBean(value.getBeanClass())).getAllList();
            allList.clear();
            EasyExcel.read(path, value.getPojoClass(), new MyExcelListener(allList)).sheet(value.getPojoClass().getSimpleName()).doReadSync();
        }
    }

    public static class MyExcelListener<T> extends AnalysisEventListener<T> {
        private List<T> tmpLst;

        public MyExcelListener(List<T> tmpLst) {
            this.tmpLst = tmpLst;
        }


        public void invoke(T data, AnalysisContext context) {
            tmpLst.add(data);
        }


        public void doAfterAllAnalysed(AnalysisContext context) {

        }
    }
}
