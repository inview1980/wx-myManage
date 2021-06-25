package com.example.mymanage.dao;

import com.example.mymanage.http.Result;
import com.example.mymanage.iface.IPersonDB;
import com.example.mymanage.pojo.PersonDetails;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service@RequiredArgsConstructor
public class PersonDao {
   private final IPersonDB iPersonDB;

    public List<PersonDetails> getPersonLst(){
        return iPersonDB.getAllList();
    }

    public Result addPerson(@NonNull PersonDetails person) {
        return iPersonDB.addPerson(person);
    }

    public boolean update(@NonNull PersonDetails person) {
        return iPersonDB.update(person);
    }

    public void deletePersonById(int id) {
        iPersonDB.delete(id);
    }

    public PersonDetails getPersonById(int personId) {
        return iPersonDB.getPersonByID(personId);
    }
}
