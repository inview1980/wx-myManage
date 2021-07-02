package com.example.mymanage.pojo;

import com.alibaba.excel.annotation.ExcelProperty;

import com.alibaba.excel.converters.booleanconverter.BooleanBooleanConverter;
import com.alibaba.excel.converters.booleanconverter.BooleanStringConverter;
import lombok.*;

/**
 * 住宅小区
 */
@ToString
@NoArgsConstructor
@Data
public class RoomDetails {
    private String _id;

    /**
     * 小区名称
     */
    @ExcelProperty(value = "小区名称")
    private String communityName;
    /**
     * 房间号
     */
    @ExcelProperty(value = "房间号")
    private String roomNumber;
    @ExcelProperty(value = "面积")
    private double roomArea;
    /**
     * 电表号
     */
    @ExcelProperty(value = "电表号")
    private String electricMeter;


    @ExcelProperty(value = "水表号")
    private String waterMeter;

    /**
     * 物业费单价
     */
    @ExcelProperty(value = "物业费单价")
    private double propertyPrice;

    @ExcelProperty(value = "是否删除的房源")
    private boolean delete;

    @ExcelProperty(value = "记录号")
    private int recordId;

}
