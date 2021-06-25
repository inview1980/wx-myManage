package com.example.mymanage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data@AllArgsConstructor@NoArgsConstructor
public class MyUser implements Cloneable{
    private int _id;
    private String userName;
    private String password;
    private String key;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return (MyUser)super.clone();
    }
}
