package com.example.mymanage.iface;

import com.example.mymanage.pojo.PayProperty;

public interface IPayPropertyDB extends IGetAllList<PayProperty> {

    boolean deletePayPropertyByID(int id);

    boolean add(PayProperty pp);

    boolean update(PayProperty pp);
}
