package com.example.tonyw.acgwarehouse.entity;

import com.example.tonyw.acgwarehouse.utils.Entity;

/**
 * Created by tonyw on 2016/12/23.
 */

public class SeriesDetailEntity extends Entity {
    public SeriesDetailEntity(int episode,String videoId) {
        super(episode,videoId);
    }

    @Override
    public int getItemType() {
        return RESOURCE_ITEM_TYPE;
    }
}