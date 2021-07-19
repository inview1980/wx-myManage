package com.example.mymanage.tool;

import com.alibaba.excel.EasyExcel;
import com.example.mymanage.pojo.MyUser;
import com.example.mymanage.pojo.RentalRecord;
import com.example.mymanage.pojo.RoomDetails;
import lombok.val;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelExportUtilTest  {

    @Test
    public void exportExport() {
        List<MyUser> users = new ArrayList<>();
        users.add(new MyUser(1, "uuuu", "2222", "3333"));
        List<RentalRecord> rrs = new ArrayList<>();
        RentalRecord rr=new RentalRecord();
        rr.setDeposit(0.44);
        rr.setPaymentDate(new Date());
        rr.setRemarks("jjjj");
        rrs.add(rr);
        val excel=new ExcelExportUtil();
        excel.add(users);
        excel.add(rrs);
        excel.build().write(new File("src/main/resources/db/tmp.xlsx"));
//         new ExcelExportUtil(users, MyUser.dbTxt.class).exportExport();
    }

    @Test
    public void writeExcel(){
        List<RoomDetails> rdLst = new ArrayList<>();
        RoomDetails rd=new RoomDetails();
        rd.setRoomNumber("333");
        rd.setDelete(true);
        rdLst.add(rd);
        RoomDetails rd2=new RoomDetails();
        rd2.setRoomNumber("4444");
        rdLst.add(rd2);
        EasyExcel.write("f:/tmp.xls").sheet("tt").head(RoomDetails.class).doWrite(rdLst);
    }
}