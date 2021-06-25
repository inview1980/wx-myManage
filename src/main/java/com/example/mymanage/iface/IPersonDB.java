package com.example.mymanage.iface;

import com.example.mymanage.http.Result;
import com.example.mymanage.pojo.PersonDetails;

public interface IPersonDB extends IGetAllList<PersonDetails> {
    Result addPerson(PersonDetails person);

    static Object object = new Object();

    boolean update(PersonDetails person);

    PersonDetails getPersonByID(int manID);

    void delete(int id);
}
