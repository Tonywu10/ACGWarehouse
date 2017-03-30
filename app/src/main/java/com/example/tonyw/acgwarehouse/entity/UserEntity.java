package com.example.tonyw.acgwarehouse.entity;

import android.app.Application;

public class UserEntity extends Application{
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
