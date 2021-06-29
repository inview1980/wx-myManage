package com.example.mymanage.tool;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.example.mymanage.db.DBChangeSignEnum;
import com.example.mymanage.iface.IGetAllList;
import com.example.mymanage.iface.IWriteToDB;
import com.example.mymanage.pojo.*;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadExcel {
    private List<PayProperty> payPropertyList;
    @Value("${resource.default-db-file}")
    private String path;
    @Autowired
    private ApplicationContext context;


    /**
     * 根据DBChangeSignEnum枚举中的值，读取相应的数据并保存到excel文件中
     */
    public InputStream saveDB2File() throws CloneNotSupportedException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        ExcelWriter excelWriter = EasyExcel.write(os).build();
        for (DBChangeSignEnum value : DBChangeSignEnum.values()) {
            List<?> allList = ((IGetAllList<?>) context.getBean(value.getBeanClass())).getAllList();
            if (value == DBChangeSignEnum.MyUser) {//如果是MyUser表，加密密码后再保存
                List<MyUser> tmpLst = new ArrayList<>();
                for (Object user : allList) {
                    MyUser u = (MyUser) ((MyUser) user).clone();
                    u.setPassword(EncryptUtil.encode(u.getPassword(), u.getKey()));
                    tmpLst.add(u);
                }
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
            EasyExcel.read(path, value.getPojoClass(), new MyExcelListener(allList)).sheet(value.getPojoClass().getSimpleName()).doReadSync();
            ((IWriteToDB) context.getBean(value.getBeanClass())).writeToDB();
        }
    }

    public void readXls() {
        for (DBChangeSignEnum value : DBChangeSignEnum.values()) {
            List<?> allList = ((IGetAllList<?>) context.getBean(value.getBeanClass())).getAllList();
            allList.clear();
            EasyExcel.read(path, value.getPojoClass(), new MyExcelListener(allList)).sheet(value.getPojoClass().getSimpleName()).doReadSync();
        }
    }

//    private void getAllList(List<?> lst,DBChangeSignEnum signEnum){
//        EasyExcel.read(path, signEnum.getPojoClass(), new MyExcelListener(lst)).sheet(signEnum.getPojoClass().getSimpleName()).doReadSync();
//    }

//    public List<PayProperty> getPayPropertyList() {
//        if (payPropertyList == null) {
//            payPropertyList = new ArrayList<>();
//            getAllList(payPropertyList,DBChangeSignEnum.PayProperty);
////            EasyExcel.read(path, PayProperty.class, new MyExcelListener(payPropertyList)).sheet(PayProperty.class.getSimpleName()).doReadSync();
//        }
//        return payPropertyList;
//    }


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
