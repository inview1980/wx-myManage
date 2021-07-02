package com.example.mymanage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.mymanage.dao.PayPropertyDao;
import com.example.mymanage.dao.PersonDao;
import com.example.mymanage.dao.RentRecordDao;
import com.example.mymanage.dao.RoomDao;
import com.example.mymanage.http.HttpResultEnum;
import com.example.mymanage.http.Result;
import com.example.mymanage.iface.IPayPropertyDB;
import com.example.mymanage.pojo.PayProperty;
import com.example.mymanage.pojo.PersonDetails;
import com.example.mymanage.pojo.RentalRecord;
import com.example.mymanage.pojo.RoomDetails;
import com.example.mymanage.tool.MyException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rent")
public class RentController {
    private final RoomDao roomDao;
    private final PersonDao personDao;
    private final RentRecordDao recordDao;
    private final PayPropertyDao proDao;

    /**
     * 获取租房的简报，包括小区、此小区的房源数量、此小区的房源面积
     *
     * @return
     */
    @PostMapping("/roomDescribe")
    Result getRoomLst() {
        return Result.Ok(roomDao.getRoomByDescribe());
    }


    /**
     * 获取指定小区的房源的基本信息，包括房号、现租户、到期日、面积、租金
     */
    @PostMapping("/roomByCommunity")
    Result getRoomByCommunityLst(@RequestBody JSONObject jsonParam) {
        String communityName = jsonParam.get("community").toString();
        return Result.Ok(roomDao.getRoomByCommunityMap(communityName));
    }

    @PostMapping("/getCommunityList")
    Result getCommunityList() {
        return Result.Ok(roomDao.getCommunitys());
    }


    @PostMapping("/updateRent")
    Result updateRent(@RequestBody JSONObject jsonObject) {
        RoomDetails room = JSON.toJavaObject(jsonObject.getJSONObject("room"), RoomDetails.class);
        PersonDetails person = JSON.toJavaObject(jsonObject.getJSONObject("person"), PersonDetails.class);
        RentalRecord record = JSON.toJavaObject(jsonObject.getJSONObject("record"), RentalRecord.class);
        if (room.get_id() != null) {
            roomDao.update(room);
        }
        if (person.get_id() != 0) {
            personDao.update(person);
        }
        if (record.get_id() != 0) {
            recordDao.update(record);
        }
        return Result.Ok();
    }

    @PostMapping("/getRoomAndRecord")
    Result getRoomAndRecord(@RequestBody JSONObject jsonObject) {
        String roomId = jsonObject.getString("roomId");
        return Result.Ok(roomDao.getRoomAndRecordMap(roomId));
    }

    @PostMapping("/continueRent")
    Result continueRent(@RequestBody JSONObject jsonObject) {
        String roomId = jsonObject.getString("roomId");
        RentalRecord record = JSON.toJavaObject(jsonObject.getJSONObject("record"), RentalRecord.class);
        if ("".equals(roomId)) return Result.Error(10001, "参数错误");
        boolean isOk;
        if(record.get_id()==0){
            isOk=roomDao.addRent(roomId, record);
        }else {
            isOk=roomDao.continueRent(roomId, record);
        }
        if (isOk)
            return Result.Ok();
        else
            return Result.Error(10000, "续租错误，刷新Room时出错");
    }

    @PostMapping("/rentRefund")
    Result rentRefund(@RequestBody JSONObject jsonObject) {
        String roomId = jsonObject.getString("roomId");
        if (roomDao.rentRefund(roomId)) return Result.Ok();
        else return Result.Error(10000, "退租出错了");
    }

    @PostMapping("/changeRentMoney")
    Result changeRentMoney(@RequestBody JSONObject jsonObject) {
        String roomId = jsonObject.getString("roomId");
        double money = jsonObject.getDouble("newValue");
        if (roomDao.changeRentMoney(roomId, money)) return Result.Ok();
        else return Result.Error(10000, "更改租金出错");
    }

    @PostMapping("/changeDeposit")
    Result changeDeposit(@RequestBody JSONObject jsonObject) {
        String roomId = jsonObject.getString("roomId");
        double money = jsonObject.getDouble("newValue");
        if (roomDao.changeDeposit(roomId, money)) return Result.Ok();
        else return Result.Error(10000, "更改押金出错");
    }

    @PostMapping("/rentRecordDescribe")
    Result rentRecordDescribe(@RequestBody JSONObject jsonObject) {
        String roomId = jsonObject.getString("roomId");
        if (roomId == null || roomId.equals("")) return Result.Error(HttpResultEnum.ParameterError);
        Map<String, Object> data = roomDao.getRentRecordDescribe(roomId);
        if (data != null && data.size() > 0) return Result.Ok(data);
        else return Result.Error(10000, "出错了，没有找到数据");
    }

    @PostMapping("/getRoomRecord")
    Result getRoomRecord(@RequestBody JSONObject jsonObject) {
        int recordId = jsonObject.getInteger("recordId");
        Map<String, Object> data = roomDao.getRoomRecord(recordId);
        return Result.Ok(data);
    }

    /**
     * 撤销退租
     */
    @PostMapping("/revokeRent")
    Result revokeRent(@RequestBody JSONObject jsonObject) {
        String roomId = jsonObject.getString("roomId");
        roomDao.revokeRent(roomId);
        return Result.Ok();
    }

    @PostMapping("/getRentDetailList")
    Result getRentDetailList() {
        return Result.Ok(roomDao.getRentDetailList());
    }

    @PostMapping("/payProperty")
    Result updateRentalRecord(@RequestBody JSONObject jsonObject) {
        RentalRecord rr = JSON.toJavaObject(jsonObject, RentalRecord.class);
        if (rr == null || rr.get_id() == 0) throw new MyException(HttpResultEnum.ParameterError);
        if (recordDao.update(rr))
            return Result.Ok();
        else
            throw new MyException(HttpResultEnum.ParameterError);
    }

    @PostMapping("/getPropertyRecord")
    Result getPropertyRecord(@RequestBody JSONObject jsonObject) {
        String roomId = jsonObject.getString("roomId");
        return Result.Ok(proDao.getPropertyRecordByRoomID(roomId));
    }

    @PostMapping("/deletePayProperty")
    Result deletePayProperty(@RequestBody JSONObject jsonObject) {
        int id = jsonObject.getInteger("id");
        if (id == 0) throw new MyException(HttpResultEnum.ParameterError);
        return Result.Ok(proDao.deletePayPropertyByID(id));
    }

    @PostMapping("/updatePayProperty")
    Result updatePayProperty(@RequestBody JSONObject jsonObject) {
        PayProperty pp = JSON.toJavaObject(jsonObject, PayProperty.class);
        if (pp == null) {
            throw new MyException(HttpResultEnum.ParameterError);
        }
        if (pp.get_id() == 0) {
            if (proDao.add(pp)) return Result.Ok();
        } else {
            if (proDao.update(pp)) return Result.Ok();
        }
       return Result.Error(HttpResultEnum.Error);
    }
}


