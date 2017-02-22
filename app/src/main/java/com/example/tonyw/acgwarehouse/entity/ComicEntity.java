package com.example.tonyw.acgwarehouse.entity;

import com.example.tonyw.acgwarehouse.utils.Entity;

/**
 * Created by tonywu10 on 2016/12/9.
 */

public class ComicEntity extends Entity {
    public ComicEntity(int thumb,String title,int episode)
    {
        super(thumb,title,episode);
    }

    @Override
    public int getItemType() {
        return COMIC_ITEM_TYPE;
    }
}
