package com.example.mymanage.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
import com.example.mymanage.tool.StaticConfigData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data@ToString
@EqualsAndHashCode
public class PayProperty {
    @ExcelProperty(value = "金额")
    private double money;

    @ExcelProperty(value = "房间id")
    private String roomID;

    @ExcelProperty(value = "付款日期")
    @DateTimeFormat(StaticConfigData.DateFormatString)@JSONField(format = StaticConfigData.DateFormatString)
    private Date payDate;

    @ExcelProperty(value = "开始时间")
    @DateTimeFormat(StaticConfigData.DateFormatString)@JSONField(format = StaticConfigData.DateFormatString)
    private Date startDate;

    @ExcelProperty(value = "期限（月）")
    private int payMonth;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "_id")
    private int _id;
}
