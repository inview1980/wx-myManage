package com.example.mymanage.tool;

import com.alibaba.excel.annotation.ExcelProperty;
import com.example.mymanage.http.HttpResultEnum;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
public class ExcelExportUtil {
    private Map<String, List<?>> data;
    //创建工作簿
    private final HSSFWorkbook wb;

    public ExcelExportUtil() {
        data = new HashMap<>();
        wb = new HSSFWorkbook();
    }

    public void add(@NonNull List<?> lst) {
        if (lst.size() == 0) return;
        data.put(lst.get(0).getClass().getSimpleName(), lst);
    }

    public ExcelExport build() {
        return new ExcelExport(wb, data);
    }


    public static class ExcelExport {
        private final HSSFWorkbook wb;
        //工作表
        private String sheetName;

        //需要填充的数据信息
        private Map<String, List<?>> data;

        public ExcelExport(HSSFWorkbook wb, Map<String, List<?>> data) {
            this.wb = wb;
            this.data = data;
        }

        public byte[] write() {
            this.writeExcel();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                wb.write(bos);
                return bos.toByteArray();
            } catch (IOException e) {
                log.error(e.getLocalizedMessage());
                throw new MyException(HttpResultEnum.IOError);
            }
        }

        public boolean write(File file) {
            this.writeExcel();
            //导出数据
            try {
                log.info("开始导出数据");
                wb.write(file);
                return true;
            } catch (IOException e) {
                log.error("导出数据失败!" + e.getLocalizedMessage());
                return false;
            }
        }

        private void writeExcel() {
            for (Map.Entry<String, List<?>> entry : this.data.entrySet()) {
                Class<?> tClass = entry.getValue().get(0).getClass();
                exportExport(getHead(tClass), getTitle(tClass), getValues(entry.getValue(), tClass), entry.getKey());
            }
            log.info("数据写入文件成功");
        }

        private <T extends Object> String[] getHead(Class<T> tClass) {
            Field[] fields = getFields(tClass);
            String[] headList = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                headList[i] = fields[i].getName();
            }
            return headList;
        }

        private <T extends Object> String[] getTitle(Class<T> tClass) {
            Field[] fields = getFields(tClass);
            String[] headList = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].isAnnotationPresent(ExcelProperty.class)) {
                    ExcelProperty excelProperty = fields[i].getAnnotation(ExcelProperty.class);
                    String value = excelProperty.value()[0];
                    headList[i] = "".equals(value) ? fields[i].getName() : value;
                } else {
                    headList[i] = fields[i].getName();
                }
            }
            return headList;
        }

        private List<Map<String, Object>> getValues(List<?> dataList, Class<?> tClass) {
            log.info("设置列表的值");
            Field[] fields = getFields(tClass);

            List<Map<String, Object>> data = new ArrayList<>();
            Map<String, Object> map;
            try {
                for (Object t : dataList) {
                    map = new HashMap<>();
                    for (Field field : fields) {
                        map.put(field.getName(), field.get(t));
                    }
                    data.add(map);
                }
                return data;
            } catch (IllegalAccessException e) {
                log.error("IllegalAccessException");
                throw new MyException(13000, "IllegalAccessException");
            }
        }

        private Field[] getFields(Class<?> tClass) {
            List<Field> fieldList = new ArrayList<>(Arrays.asList(tClass.getDeclaredFields()));
            fieldList.removeIf(f -> Modifier.isStatic(f.getModifiers()));
            fieldList.forEach(f -> f.setAccessible(true));
            return fieldList.toArray(new Field[0]);
        }

        /**
         * 开始导出数据信息
         */
        private void exportExport(String[] headList, String[] titleList, List<Map<String, Object>> data,
                                  String sheetName) {
            log.info("创建工作簿");
            //创建工作表
            HSSFSheet wbSheet = wb.createSheet(sheetName);
            log.info("创建工作表");

            //在第行创建rows
            HSSFRow row = wbSheet.createRow(0);
            //设置列头元素
            HSSFCell cellHead;
            for (int i = 0; i < titleList.length; i++) {
                cellHead = row.createCell(i);
                cellHead.setCellValue(titleList[i]);
            }
            log.info("开始写入实体数据信息");

            //开始写入实体数据信息
            int a = 1;
            for (int i = 0; i < data.size(); i++) {
                HSSFRow roww = wbSheet.createRow((int) a);
                Map<String, Object> map = data.get(i);
                HSSFCell cell = null;
                for (int j = 0; j < headList.length; j++) {
                    cell = roww.createCell(j);
                    Object valueObject = map.get(headList[j]);
                    Object value = null;
                    if (valueObject == null) {
                        valueObject = "";
                    }
                    if (valueObject instanceof String) {
                        //取出的数据是字符串直接赋值
                        cell.setCellValue(  valueObject.toString());
                    } else if (valueObject instanceof Integer) {
                        //取出的数据是Integer
                        cell.setCellValue((Integer) valueObject);
                    } else if (valueObject instanceof BigDecimal) {
                        //取出的数据是BigDecimal
                        cell.setCellValue(((BigDecimal)valueObject).toString());
                    } else if (valueObject instanceof Date) {
                        cell.setCellValue( DateUtils.dateFormat.format(valueObject));
                    }else if(valueObject instanceof Double){
                        cell.setCellValue((double)valueObject);
                    } else {
                        cell.setCellValue( valueObject.toString());
                    }
//                    cell.setCellValue(value);
                }
                a++;
            }
        }
    }
}