package com.example.tonyw.acgwarehouse.entity;

import android.app.Application;
import android.graphics.Bitmap;

/**
 * Created by tonyw on 2017/2/17.
 */

public class UserEntity extends Application{
    private String userName;
    private Bitmap userAvatar;

    public Bitmap getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(Bitmap userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
