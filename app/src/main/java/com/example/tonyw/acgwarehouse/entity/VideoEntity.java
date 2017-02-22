package com.example.tonyw.acgwarehouse.entity;

import android.graphics.Bitmap;

import com.example.tonyw.acgwarehouse.utils.Entity;

import java.util.Date;

/**
 * Created by tonywu10 on 2016/11/28.
 */

public class VideoEntity extends Entity {
    public VideoEntity(Bitmap thumb, String title,String url,String intro,int allepisode)
    {
        super(thumb,title,url,intro,allepisode);
    }

    public VideoEntity()
    {

    }

    @Override
    public int getItemType() {
        return VIDEO_ITEM_TYPE;
    }
}