package com.example.mymanage.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data@ToString
@EqualsAndHashCode
public class PayProperty {
    private static final String dateFormat = "yyyy-M-d";

    @ExcelProperty(value = "金额")
    private double money;

    @ExcelProperty(value = "房间id")
    private String roomID;

    @ExcelProperty(value = "付款日期")
    @DateTimeFormat(dateFormat)@JSONField(format = dateFormat)
    private Date payDate;

    @ExcelProperty(value = "开始时间")
    @DateTimeFormat(dateFormat)@JSONField(format = dateFormat)
    private Date startDate;

    @ExcelProperty(value = "期限（月）")
    private int payMonth;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "_id")
    private int _id;
}
