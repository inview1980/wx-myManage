package com.example.mymanage.db;

import com.example.mymanage.AppConfig;
import com.example.mymanage.iface.IReadAndWriteDB;
import com.example.mymanage.iface.IRentRecordDB;
import com.example.mymanage.iface.IWriteToDB;
import com.example.mymanage.pojo.RentalRecord;
import com.example.mymanage.tool.FileDBUtil;
import com.example.mymanage.tool.TimedTask;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j@Component
public class RentRecordHttp implements IRentRecordDB, IWriteToDB {
    private static List<RentalRecord> rentalRecordList;

    @Override
    @Synchronized
    public List<RentalRecord> getAllList() {
        if (rentalRecordList == null) {
            rentalRecordList = AppConfig.getiReadAndWriteDB().getListFromDB(RentalRecord.class);
        }
        return rentalRecordList;
    }

    @Override
    public RentalRecord getRecordById(int recordId) {
        synchronized (object) {
            return getAllList().stream().filter(re -> re.get_id() == recordId).findFirst().orElse(null);
        }
    }

    @Override
    public int add(@NonNull RentalRecord record) {
        int maxId = 0;
        synchronized (object) {
            maxId = getAllList().stream().mapToInt(RentalRecord::get_id).max().orElse(0);
            maxId++;
            record.set_id(maxId);
            getAllList().add(record);
            TimedTask.SetTableChanged(DBChangeSignEnum.RentRecordSign);
            log.info("增加记录号,房间为：" + record.getRoomNumber());
        }
        return maxId;
    }

    @Override
    public boolean changeRentMoney(int recordId, double money) {
        if (recordId == 0) return false;
        synchronized (object) {
            for (RentalRecord record : getAllList()) {
                if (record.get_id() == recordId) {
                    record.setMonthlyRent(money);
                    TimedTask.SetTableChanged(DBChangeSignEnum.RentRecordSign);
                    log.info("调整租金,房间为：" + record.getRoomNumber());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean changeDeposit(int recordId, double money) {
        if (recordId == 0) return false;
        synchronized (object) {
            for (RentalRecord record : getAllList()) {
                if (record.get_id() == recordId) {
                    record.setDeposit(money);
                    TimedTask.SetTableChanged(DBChangeSignEnum.RentRecordSign);
                    log.info("调整押金,房间为：" + record.getRoomNumber());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<RentalRecord> getRecordsByRoomNumber(@NonNull String roomNumber) {
        return getAllList().stream().filter(rr -> roomNumber.equals(rr.getRoomNumber())).collect(Collectors.toList());
    }


    @Override
    public boolean update(@NonNull RentalRecord record) {
        synchronized (object) {
            getAllList().removeIf(rd -> rd.get_id() == record.get_id());
            getAllList().add(record);
            TimedTask.SetTableChanged(DBChangeSignEnum.RentRecordSign);
            log.info("更新租房记录：" + record.getRoomNumber());
        }
        return true;
    }

    @Override
    public boolean writeToDB() {
        return AppConfig.getiReadAndWriteDB().writeToDB(rentalRecordList);// HttpUtil.writeToDB(rentalRecordList, TableName);
    }

    @Override
    public void removeDB() {
        rentalRecordList = null;
        log.info("数据已清空");

    }
}
