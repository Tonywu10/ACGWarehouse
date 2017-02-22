package com.example.tonyw.acgwarehouse.entity;

import com.example.tonyw.acgwarehouse.utils.Entity;

/**
 * Created by tonywu10 on 2016/12/13.
 */

public class ResourceEntity extends Entity {
    public ResourceEntity(int thumb, String title,int episode) {
        super(thumb, title, episode);
    }

    @Override
    public int getItemType() {
        return RESOURCE_ITEM_TYPE;
    }
}
