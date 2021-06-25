package com.example.mymanage.dao;

import com.example.mymanage.http.HttpResultEnum;
import com.example.mymanage.iface.IPersonDB;
import com.example.mymanage.iface.IRentRecordDB;
import com.example.mymanage.iface.IRoomDB;
import com.example.mymanage.pojo.PersonDetails;
import com.example.mymanage.pojo.RentalRecord;
import com.example.mymanage.pojo.RoomDetails;
import com.example.mymanage.tool.DateUtils;
import com.example.mymanage.tool.MyException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service@RequiredArgsConstructor
public class RoomDao {
    private final IRoomDB iRoomDB;
    private final IRentRecordDB iRentRecordDB;
    private final IPersonDB iPersonDB;
    //按中文排序
    private final static Comparator<Object> CHINA_COMPARE = Collator.getInstance(java.util.Locale.CHINA);

    /**
     * 获取房间的列表，除去“已删除”项
     */
    public List<RoomDetails> getRoomDetails() {
        return iRoomDB.getAllList().stream().filter(room -> !room.isDelete())
                .sorted((r1, r2) -> CHINA_COMPARE.compare(r1.getCommunityName(), r2.getCommunityName()))//中文汉字排序
                .collect(Collectors.toList());
    }

    public ArrayList<HashMap<String, String>> getRoomByCommunityMap(@NonNull String communityName) {
        val resultLst = new ArrayList<HashMap<String, String>>();
        Stream<RoomDetails> lst;
        if (communityName.equals("全部"))
            lst = getRoomDetails().stream();
        else
            lst = getRoomDetails().stream().filter(room -> room.getCommunityName().equals(communityName));
        for (RoomDetails roomDetails : lst.filter(rd -> !rd.isDelete()).collect(Collectors.toList())) {
            val map = new HashMap<String, String>();
            map.put("roomId", roomDetails.get_id());
            map.put("community", roomDetails.getCommunityName());
            map.put("roomNumber", roomDetails.getRoomNumber());
            map.put("area", roomDetails.getRoomArea() + "");
            RentalRecord record =
                    iRentRecordDB.getAllList().stream().filter(rec -> rec.get_id() == roomDetails.getRecordId()).findFirst().orElse(new RentalRecord());
            map.put("monthlyRent", record.getMonthlyRent() + "");
            if (record.getStartDate() == null) {
                map.put("endDate", "");
            } else {
                map.put("endDate", DateUtils.dateFormat.format(DateUtils.DateAdd(record.getStartDate(),
                        record.getPayMonth())));
            }
            String personName =
                    iPersonDB.getAllList().stream().filter(per -> per.get_id() == record.getManID()).map(PersonDetails::getName)
                            .findFirst().orElse("");
            map.put("personName", personName);
            map.put("personId", record.getManID() + "");
            resultLst.add(map);
        }
        return resultLst;
    }

    public ArrayList<HashMap<String, String>> getRoomByDescribe() {
        List<RoomDetails> tmp = iRoomDB.getAllList().stream().filter(room -> !room.isDelete()).collect(Collectors.toList());
        val resultLst = new ArrayList<HashMap<String, String>>();
        tmp.stream().map(RoomDetails::getCommunityName).distinct().sorted(CHINA_COMPARE).forEach(c -> {
            val map = new HashMap<String, String>();
            map.put("communityName", c);
            map.put("roomCount",
                    tmp.stream().map(RoomDetails::getCommunityName).filter(name -> name.equals(c)).count() + "");
            map.put("areaTotal",
                    tmp.stream().filter(room -> room.getCommunityName().equals(c)).mapToDouble(RoomDetails::getRoomArea).sum() + "");
            resultLst.add(map);
        });
        val map = new HashMap<String, String>();
        map.put("communityName", "全部");
        map.put("roomCount", tmp.size() + "");
        map.put("areaTotal", tmp.stream().mapToDouble(RoomDetails::getRoomArea).sum() + "");
        resultLst.add(map);
        return resultLst;
    }

    public List<String> getCommunitys() {
        return iRoomDB.getAllList().stream().map(RoomDetails::getCommunityName).distinct()
                .sorted(CHINA_COMPARE).collect(Collectors.toList());
    }

    public boolean addRoom(@NonNull RoomDetails rd) {
        return iRoomDB.addRoom(rd);
    }

    public boolean deleteRoom(@NonNull String id) {
        return iRoomDB.moveToDelete(id);
    }

    public Map<String, Object> getRoomDetailById(@NonNull String roomId) {
        Stream<RoomDetails> tmpLst =
                iRoomDB.getAllList().stream().filter(rd -> !rd.isDelete() && roomId.equals(rd.get_id()));

        Map<String, Object> map = new HashMap<>();
        tmpLst.findFirst().ifPresent(room -> {
            map.put("room", room);
            val record = iRentRecordDB.getAllList().stream().filter(rr -> rr.get_id() == room.getRecordId()).findFirst();
            if (record.isPresent()) {
                map.put("record", record.get());
                iPersonDB.getAllList().stream().filter(pd -> pd.get_id() == record.get().getManID()).findFirst()
                        .ifPresent(pd -> map.put("person", pd));
            }
        });
        return map;
    }

    public boolean update(@NonNull RoomDetails room) {
        return iRoomDB.updateRoom(room);
    }

    public Map<String, Object> getRoomAndRecordMap(@NonNull String roomId) {
        Map<String, Object> result = new HashMap<>();
        val room = iRoomDB.getByRoomId(roomId);
        result.put("room", room);
        RentalRecord record = iRentRecordDB.getRecordById(room.getRecordId());
        result.put("record", record);
        return result;
    }

    public boolean continueRent(@NonNull String roomId,@NonNull RentalRecord record) {
         iRentRecordDB.update(record);
        return iRoomDB.updateRoomId(roomId, record.get_id());
    }

    public boolean addRent(@NonNull String roomId,@NonNull RentalRecord record) {
        int recordId = iRentRecordDB.add(record);
        return iRoomDB.updateRoomId(roomId, recordId);
    }

    public boolean rentRefund(@NonNull String roomId) {
        return iRoomDB.rentRefund(roomId);
    }

    public boolean changeRentMoney(@NonNull String roomId, double money) {
        int recordId = iRoomDB.getByRoomId(roomId).getRecordId();
        return iRentRecordDB.changeRentMoney(recordId, money);
    }

    public boolean changeDeposit(@NonNull String roomId, double money) {
        int recordId = iRoomDB.getByRoomId(roomId).getRecordId();
        return iRentRecordDB.changeDeposit(recordId, money);
    }

    public Map<String, Object> getRentRecordDescribe(@NonNull String roomId) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, String>> result = new ArrayList<>();
        RoomDetails room = iRoomDB.getByRoomId(roomId);
        if (room == null) return null;
        resultMap.put("room", room);
        List<RentalRecord> records = iRentRecordDB.getAllList().stream()
                .filter(rm -> room.getRoomNumber().equals(rm.getRoomNumber()))
                .sorted((t1, t2) -> {
                    if (t1.getStartDate() == null) return -1;
                    if (t2.getStartDate() == null) return 1;
                    return Long.compare(t1.getStartDate().getTime(), t2.getStartDate().getTime());
                }).collect(Collectors.toList());
        Map<String, String> map = null;
        for (RentalRecord rr : records) {
            map = new HashMap<>();
            map.put("dateSlot", getDate(rr.getStartDate(), rr.getPayMonth()));
            map.put("recordId", rr.get_id() + "");
            PersonDetails pd = iPersonDB.getPersonByID(rr.getManID());
            map.put("company", pd.getCompany());
            map.put("person", pd.getName());
            result.add(map);
        }
        resultMap.put("recordDescribe", result);
        return resultMap;
    }

    private String getDate(Date date, int month) {
        if (date == null) return "";
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        String result = c1.get(Calendar.YEAR) + "-" + (c1.get(Calendar.MONTH) + 1) + "-" + c1.get(Calendar.DAY_OF_MONTH) + "~";
        c1.add(Calendar.MONTH, month);
        result += c1.get(Calendar.YEAR) + "-" + (c1.get(Calendar.MONTH) + 1) + "-" + c1.get(Calendar.DAY_OF_MONTH);
        return result;
    }

    public Map<String, Object> getRoomRecord(int recordId) {
        Map<String, Object> resultMap = new HashMap<>();
        RentalRecord rr = iRentRecordDB.getRecordById(recordId);
        if (rr == null) throw new MyException(10000, "没有找到指定的租房记录");
        resultMap.put("record", rr);
        RoomDetails rd = iRoomDB.getByRoomByRoomNumber(rr.getRoomNumber());
        resultMap.put("room", rd);
        PersonDetails pd = iPersonDB.getPersonByID(rr.getManID());
        resultMap.put("person", pd);
        return resultMap;
    }

    public void revokeRent(@NonNull String roomId) {
        RoomDetails room = iRoomDB.getByRoomId(roomId);
        int id =
                iRentRecordDB.getRecordsByRoomNumber(room.getRoomNumber()).stream().mapToInt(RentalRecord::get_id).max().orElse(0);
        iRoomDB.updateRoomId(roomId, id);
    }

    public List<RoomDetails> getDeletedRooms() {
        return iRoomDB.getAllList().stream().filter(RoomDetails::isDelete).collect(Collectors.toList());
    }

    public void deleteCompletelyById(@NonNull String roomId) {
        if (!iRoomDB.deleteRoom(roomId)) {
            throw new MyException(10000, "彻底删除房间失败");
        }
    }

    public void restoreDeleteRoom(@NonNull String roomId) {
        if ( roomId.equals(""))
            throw new MyException(HttpResultEnum.ParameterError);
        if (!iRoomDB.restoreDeleteRoom(roomId)) {
            throw new MyException(10000, "恢复删除房间失败");
        }
    }

    public List<Map<String, Object>> getRentDetailList() {
        List<Map<String, Object>> resultLst = new ArrayList<>();
        Map<String, Object> map;
        double totalMoney = 0;
        for (RoomDetails room : getRoomDetails()) {
            map = new HashMap<>();
            map.put("room", room);
            RentalRecord rr = iRentRecordDB.getRecordById(room.getRecordId());
            if (rr != null) {
                map.put("monthlyRent", rr.getMonthlyRent());
                totalMoney += rr.getMonthlyRent();
            }
            resultLst.add(map);
        }
        //统计合计数据
        map = new HashMap<>();
        map.put("monthlyRent", totalMoney);
        Map<String, Object> roomMap = new HashMap<>();
        roomMap.put("communityName", "合计");
        roomMap.put("roomArea", getRoomDetails().stream().mapToDouble(RoomDetails::getRoomArea).sum());
        map.put("room", roomMap);
        resultLst.add(map);
        return resultLst;
    }
}
