package com.ankushgrover.superlog.db.listener;

public interface DataLoadListener<T> {

    void onDataLoaded(T obj, int key);

}