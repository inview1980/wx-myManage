package com.example.mymanage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.mymanage.dao.PersonDao;
import com.example.mymanage.http.Result;
import com.example.mymanage.pojo.PersonDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController@RequiredArgsConstructor
@RequestMapping("/rent")
public class RentPersonController {
    private final PersonDao personDao;
//    private final RoomDao roomDao;

    @PostMapping("/getPersonList")
    Result getPersonList() {
        return Result.Ok(personDao.getPersonLst());
    }

    @PostMapping("/addPerson")
    Result addPerson(@RequestBody JSONObject jsonObject) {
        PersonDetails person = JSON.toJavaObject(jsonObject, PersonDetails.class);
        return personDao.addPerson(person);
    }

    @PostMapping("/modifyPerson")
    Result modifyPerson(@RequestBody JSONObject jsonObject) {
        PersonDetails person = JSON.toJavaObject(jsonObject, PersonDetails.class);
        personDao.update(person);
        return Result.Ok();
    }

    @PostMapping("/deletePerson")
    Result deletePerson(@RequestBody JSONObject jsonObject) {
        int id = jsonObject.getInteger("personId");
        personDao.deletePersonById(id);
        return Result.Ok();
    }

    @PostMapping("/getPersonById")
    Result getPersonById(@RequestBody JSONObject jsonObject) {
        int personId=jsonObject.getInteger("personId");
        return Result.Ok(personDao.getPersonById(personId));
    }
}
