package com.example.mymanage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.mymanage.dao.RoomDao;
import com.example.mymanage.http.HttpResultEnum;
import com.example.mymanage.http.Result;
import com.example.mymanage.pojo.RoomDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rent")
public class RentRoomController {
    private final RoomDao roomDao;

    @PostMapping("/addRoom")
    Result addRoom(@RequestBody JSONObject jsonObject) {
        RoomDetails rd = JSON.toJavaObject(jsonObject, RoomDetails.class);
        if (roomDao.addRoom(rd)) return Result.Ok();
        else return Result.Error(HttpResultEnum.Error);
    }

    /**
     * 将房间移入已删除名单
     */
    @PostMapping("/deleteRoom")
    Result deleteRoom(@RequestBody JSONObject jsonObject) {
        String id = jsonObject.getString("roomId");
        if (roomDao.deleteRoom(id)) {
            return Result.Ok();
        }
        return Result.Error(HttpResultEnum.Error);
    }

    @PostMapping("/getRoomDetail")
    Result getRoomDetail(@RequestBody JSONObject jsonObject) {
        String roomId = jsonObject.getString("roomId");
        return Result.Ok(roomDao.getRoomDetailById(roomId));
    }

    @PostMapping("/getDeletedRooms")
    Result getDeletedRooms() {
        return Result.Ok(roomDao.getDeletedRooms());
    }

    @PostMapping("/deleteCompletely")
    Result deleteCompletely(@RequestBody JSONObject jsonObject) {
        String roomId = jsonObject.getString("roomId");
        roomDao.deleteCompletelyById(roomId);
        return Result.Ok();
    }

    @PostMapping("/restoreDeleteRoom")
    Result restoreDeleteRoom(@RequestBody JSONObject jsonObject) {
        String roomId = jsonObject.getString("roomId");
        roomDao.restoreDeleteRoom(roomId);
        return Result.Ok();
    }

    @PostMapping("/getRoomList")
    Result getRoomList() {
        return Result.Ok(roomDao.getRoomDetails());
    }
}
