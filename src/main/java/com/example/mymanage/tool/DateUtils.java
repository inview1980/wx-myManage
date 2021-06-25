package com.example.mymanage.tool;

import lombok.NonNull;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static SimpleDateFormat dateFormat=new SimpleDateFormat(StateData.getDateFormat());
    public static Date DateAdd(@NonNull Date date, int month){
        if(date==null) date=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,month);
        return calendar.getTime();
    }
}
