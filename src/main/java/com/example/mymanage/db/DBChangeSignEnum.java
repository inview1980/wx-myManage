package com.example.mymanage.db;

import com.example.mymanage.iface.*;
import com.example.mymanage.pojo.*;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;


@AllArgsConstructor
public enum DBChangeSignEnum {
    PersonSign(false, new PersonHttp(), PersonDetails.class, IPersonDB.class),
    RoomSign(false, new RoomHttp(), RoomDetails.class, IRoomDB.class),
    PayProperty(false, new PayPropertyHttp(), com.example.mymanage.pojo.PayProperty.class, PayPropertyHttp.class),
    MyUser(false, new MyUserHttp(), com.example.mymanage.pojo.MyUser.class, MyUserHttp.class),
    RentRecordSign(false, new RentRecordHttp(), RentalRecord.class, IRentRecordDB.class);

    private boolean isChange;
    private final IWriteToDB writeToDB;
    private final Class<?> pojoClass;

    public Class<?> getPojoClass() {
        return pojoClass;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    private final Class<?> beanClass;

    public void isChanged(@NotNull Object object) {
        synchronized (object) {
            this.isChange = true;
        }
    }

    public void checkChanged() {
        if (isChange) {
            isChange = !writeToDB.writeToDB();
        }
    }
}
