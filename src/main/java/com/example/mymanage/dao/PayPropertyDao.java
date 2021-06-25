package com.example.mymanage.dao;

import com.example.mymanage.http.HttpResultEnum;
import com.example.mymanage.http.Result;
import com.example.mymanage.iface.IPayPropertyDB;
import com.example.mymanage.pojo.PayProperty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service@RequiredArgsConstructor
public class PayPropertyDao {
    private final IPayPropertyDB iPayPropertyDB;

    public List<PayProperty> getPropertyRecordByRoomID(@NonNull String roomId) {
        return iPayPropertyDB.getAllList().stream().filter(pro->roomId.equals(pro.getRoomID()))
                .sorted(Comparator.comparingLong(p->p.getPayDate().getTime())).collect(Collectors.toList());
    }

    public Result deletePayPropertyByID(int id) {
        if(iPayPropertyDB.deletePayPropertyByID(id)){
            return Result.Ok();
        }else {
            return Result.Error(HttpResultEnum.Error);
        }
    }

    public boolean add(PayProperty pp) {
        return iPayPropertyDB.add(pp);
    }

    public boolean update(PayProperty pp) {
        return iPayPropertyDB.update(pp);
    }
}
