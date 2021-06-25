package com.example.mymanage.iface;

import com.example.mymanage.pojo.RoomDetails;

import java.util.List;

public interface IRoomDB extends IGetAllList<RoomDetails> {
    static Object object = new Object();


    boolean addRoom(RoomDetails roomDetails);
    boolean updateRoom(RoomDetails roomDetails);
    boolean deleteRoom(String _id);
    boolean moveToDelete(String _id);
    void deleteRooms(List<String> rooms);

    /**
     * 将指定房间ID的rocordId更改
     * @param roomId
     * @param recordId
     * @return
     */
    boolean updateRoomId(String roomId, int recordId);

    RoomDetails getByRoomId(String roomId);

    boolean rentRefund(String roomId);

    RoomDetails getByRoomByRoomNumber(String roomNumber);

    boolean restoreDeleteRoom(String roomId);
}
