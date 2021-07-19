package com.example.mymanage.iface;

import lombok.NonNull;

import java.util.List;

public interface IReadAndWriteDB {
    <T> List<T> getListFromDB(@NonNull Class<T> tClass);
    <T> boolean writeToDB(@NonNull List<T> tList);
}
