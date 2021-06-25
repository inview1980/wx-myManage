package com.example.mymanage.iface;

import com.example.mymanage.pojo.RentalRecord;

import java.util.List;

public interface IRentRecordDB extends IGetAllList<RentalRecord> {
    static Object object = new Object();
    boolean update(RentalRecord record);


    RentalRecord getRecordById(int recordId);

    int add(RentalRecord record);

    boolean changeRentMoney(int recordId, double money);

    boolean changeDeposit(int recordId, double money);

    List<RentalRecord> getRecordsByRoomNumber(String roomNumber);
}
