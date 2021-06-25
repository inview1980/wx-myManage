package com.example.mymanage.dao;

import com.example.mymanage.iface.IRentRecordDB;
import com.example.mymanage.pojo.RentalRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service@RequiredArgsConstructor
public class RentRecordDao {
    private final IRentRecordDB iRentRecordDB;

    public List<RentalRecord> getRentalRecordLst(){
        return iRentRecordDB.getAllList();
    }

    public boolean update(RentalRecord record) {
       return iRentRecordDB.update(record);
    }
}
