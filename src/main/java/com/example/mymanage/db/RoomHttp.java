package com.example.mymanage.db;

import com.example.mymanage.AppConfig;
import com.example.mymanage.iface.IReadAndWriteDB;
import com.example.mymanage.iface.IRoomDB;
import com.example.mymanage.iface.IWriteToDB;
import com.example.mymanage.pojo.RoomDetails;
import com.example.mymanage.tool.FileDBUtil;
import com.example.mymanage.tool.MyException;
import com.example.mymanage.tool.TimedTask;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j@Component
public class RoomHttp implements IRoomDB, IWriteToDB {
    private static List<RoomDetails> roomDetailsList;

    @Override@Synchronized
    public List<RoomDetails> getAllList() {
        if (roomDetailsList == null) {
            roomDetailsList = AppConfig.getiReadAndWriteDB().getListFromDB(RoomDetails.class);
        }
        return roomDetailsList;
    }

    @Override
    public boolean addRoom(RoomDetails roomDetails) {
        synchronized (object) {
            val room =
                    getAllList().stream().filter(rd ->
                            rd.getRoomNumber().equals(roomDetails.getRoomNumber()) && rd.getCommunityName().equals(roomDetails.getCommunityName())).findFirst();
            if (room.isPresent()) return false;//新增加的房号已存在

            String roomId=new Date().getTime()+"";
            roomDetails.set_id(roomId);
            roomDetailsList.add(roomDetails);
            log.info("增加房源：" + roomDetails.getRoomNumber());
            TimedTask.SetTableChanged(DBChangeSignEnum.RoomSign);
        }
        return true;
    }

    @Override
    public boolean updateRoom(RoomDetails roomDetails) {
        Optional.ofNullable(getAllList()).ifPresent(rooms -> {
            //先删除，再增加
            deleteRoom(roomDetails.get_id());
            synchronized (object) {
                rooms.add(roomDetails);
                log.info("更新房源：" + roomDetails.getRoomNumber());
                TimedTask.SetTableChanged(DBChangeSignEnum.RoomSign);
            }
        });
        return true;
    }

    @Override
    public boolean deleteRoom(@NonNull String _id) {
        Optional.ofNullable(getAllList()).ifPresent(rooms -> {
            synchronized (object) {
                rooms.removeIf(room -> room.get_id().equals(_id));
                TimedTask.SetTableChanged(DBChangeSignEnum.RoomSign);
                log.info("砌底删除房源,id=：" + _id);
            }
        });
        return true;
    }

    @Override
    public boolean moveToDelete(@NonNull String id) {
        for (RoomDetails room : getAllList()) {
            synchronized (object) {
                if (room.get_id().equals(id)) {
                    room.setDelete(true);
                    TimedTask.SetTableChanged(DBChangeSignEnum.RoomSign);
                    log.info("将房源id=：" + id + "移入已删除列表");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void deleteRooms(@NonNull List<String> rooms) {
        Optional.ofNullable(getAllList()).ifPresent(roomLst -> {
            synchronized (object) {
                for (String room : rooms) {
                    roomLst.removeIf(rm -> rm.getRoomNumber().equals(room));
                    log.info("砌底删除房源：" + room);
                }
                TimedTask.SetTableChanged(DBChangeSignEnum.RoomSign);
            }
        });
    }

    @Override
    public RoomDetails getByRoomId(@NonNull String roomId) {
        if (!StringUtils.isEmpty(roomId) && getAllList() != null) {
            synchronized (object) {
                return getAllList().stream().filter(rm -> rm.get_id().equals(roomId)).findFirst().orElse(null);
            }
        }
        return null;
    }

    @Override
    public boolean rentRefund(@NonNull String roomId) {
        synchronized (object){
            for (RoomDetails room : getAllList()) {
                if(room.get_id().equals(roomId)){
                    room.setRecordId(0);
                    TimedTask.SetTableChanged(DBChangeSignEnum.RoomSign);
                    log.info("修改房间的记录号=0" );
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public RoomDetails getByRoomByRoomNumber(@NonNull String roomNumber) {
        synchronized (object){
            return getAllList().stream().filter(rd -> rd.getRoomNumber().equals(roomNumber)).findFirst()
                    .orElseThrow(new MyException(10000, "没有找到指定的房源"));
        }
    }

    @Override
    public boolean restoreDeleteRoom(@NonNull String roomId) {
        synchronized (object){
            for (RoomDetails room : getAllList()) {
                if(roomId.equals(room.get_id())){
                    room.setDelete(false);
                    TimedTask.SetTableChanged(DBChangeSignEnum.RoomSign);
                    log.info("房间" + room.getRoomNumber() + "已移出已删除列表");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean updateRoomId(@NonNull String roomId,int recordId) {
        synchronized (object){
            for (int i = 0; i < getAllList().size(); i++) {
                if(getAllList().get(i).get_id().equals(roomId)){
                    getAllList().get(i).setRecordId(recordId);
                    TimedTask.SetTableChanged(DBChangeSignEnum.RoomSign);
                    log.info("修改房间的记录号:" + recordId);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean writeToDB() {
        return AppConfig.getiReadAndWriteDB().writeToDB(roomDetailsList);
    }

    @Override
    public void removeDB() {
        log.info("数据已清空");
        roomDetailsList=null;
    }
}
