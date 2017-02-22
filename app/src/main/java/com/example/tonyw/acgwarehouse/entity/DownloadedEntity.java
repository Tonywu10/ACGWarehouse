package com.example.tonyw.acgwarehouse.entity;

import com.example.tonyw.acgwarehouse.utils.Entity;

/**
 * Created by tonywu10 on 2016/12/10.
 */

public class DownloadedEntity extends Entity {
    public DownloadedEntity(int thumb, String title, long size, String type) {
        super(thumb, title, size, type);
    }

    @Override
    public int getItemType() {
        return DOWNLOADED_ITEM_TYPE;
    }
}
