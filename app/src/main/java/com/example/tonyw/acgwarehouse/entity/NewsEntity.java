package com.example.tonyw.acgwarehouse.entity;

import android.graphics.Bitmap;

import com.example.tonyw.acgwarehouse.utils.Entity;

import java.util.Date;

/**
 * Created by tonywu10 on 2016/11/29.
 */

public class NewsEntity extends Entity {
    public NewsEntity(String title, Bitmap thumb, Date date, String source,String url) {
        super(title, thumb, date, source,url);
    }

    public NewsEntity()
    {
        super();
    }

    @Override
    public int getItemType() {
        return NEWS_ITEM_TYPE;
    }
}
