package com.example.mymanage.pojo;

import com.alibaba.excel.annotation.ExcelProperty;

import java.util.Calendar;
import java.util.Date;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

/**
 * 租房记录
 */
@ToString
@EqualsAndHashCode
@Data
public final class RentalRecord {
    private static final String dateFormat = "yyyy-M-d";
    @ExcelProperty(value = "primary_id")
    private int _id;

    @ExcelProperty(value = "房租开始时间")
    @DateTimeFormat(dateFormat)
    @JSONField(format = dateFormat)
    private Date startDate;

    @ExcelProperty(value = "房租时长（月）")
    private int payMonth;


    @ExcelProperty(value = "付款日期")
    @DateTimeFormat(dateFormat)
    @JSONField(format = dateFormat)
    private Date paymentDate;

    @ExcelProperty(value = "付款金额")
    private double totalMoney;

    @ExcelProperty(value = "物业费")
    private double propertyCosts;

    /**
     * 是否包含物业费
     */
    @ExcelProperty(value = "房租包含物业费")
    private boolean isContainRealty;

    @ExcelProperty(value = "房间号")
    private String roomNumber;

    /**
     * 物业费开始时间
     */
    @ExcelProperty(value = "物业费开始时间")
    @DateTimeFormat(dateFormat)
    @JSONField(format = dateFormat)
    private Date realtyStartDate;

    /**
     * 物业费时长
     */
    @ExcelProperty(value = "物业费时长（月）")
    private int propertyTime;


    @ExcelProperty(value = "用户id")
    private int manID;
    /**
     * 押金
     */
    @ExcelProperty(value = "押金")
    private double deposit;

    /**
     * 月租金
     */
    @ExcelProperty(value = "月租金")
    private double monthlyRent;

    /**
     * 签合同时间
     */
    @ExcelProperty(value = "签合同时间")
    @DateTimeFormat(dateFormat)
    @JSONField(format = dateFormat)
    private Date contractSigningDate;

    /**
     * 合同期限（月）
     */
    @ExcelProperty(value = "合同期限（月）")
    private int contractMonth;

    @ExcelProperty(value = "备注")
    private String remarks;
}
