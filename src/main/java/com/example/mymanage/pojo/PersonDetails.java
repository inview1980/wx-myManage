package com.example.mymanage.pojo;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.*;

/**
 * 租户、联系人资料
 */
@Data
@NoArgsConstructor
public final class PersonDetails {
    @ExcelProperty(value = "_id")
    private int _id;

    @ExcelProperty(value = "姓名")
    private String name;
    @ExcelProperty(value = "电话")
    private String tel;
    /**
     * 身份证号码
     */
    @ExcelProperty(value = "证件号")
    private String card;
    @ExcelProperty(value = "其他")
    private String other;
    /**
     * 公司
     */
    @ExcelProperty(value = "公司")
    private String company;

    public PersonDetails(@NonNull String name) {
        this.name = name;
    }
}
