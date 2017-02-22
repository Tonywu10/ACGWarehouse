package com.example.tonyw.acgwarehouse.entity;

import com.example.tonyw.acgwarehouse.utils.Entity;

/**
 * Created by tonywu10 on 2016/12/13.
 */

public class CollectComicEntity extends Entity {
    public CollectComicEntity(int thumb,String title,int episode,boolean isUpdate)
    {
        super(thumb,title,episode,isUpdate);
    }

    @Override
    public int getItemType() {
        return COLLECT_COMIC_ITEM_TYPE;
    }
}

