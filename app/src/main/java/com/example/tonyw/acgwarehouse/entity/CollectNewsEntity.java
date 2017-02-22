package com.example.tonyw.acgwarehouse.entity;

import android.graphics.Bitmap;

import com.example.tonyw.acgwarehouse.utils.Entity;

import java.util.Date;

/**
 * Created by tonywu10 on 2016/12/13.
 */

public class CollectNewsEntity extends Entity {
    public CollectNewsEntity(String title, Bitmap thumb, Date date, String source)
    {
        super(title, thumb, date, source);
    }

    @Override
    public int getItemType() {
        return COLLECT_NEWS_ITEM_TYPE;
    }
}
