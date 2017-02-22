package com.example.tonyw.acgwarehouse.entity;

import com.example.tonyw.acgwarehouse.utils.Entity;

/**
 * Created by tonywu10 on 2016/12/12.
 */

public class DownloadingEntity extends Entity {
    public DownloadingEntity(int thumb, String title, int progress, long size) {
        super(thumb, title, progress,size);
    }

    @Override
    public int getItemType() {
        return DOWNLOADING_ITEM_TYPE;
    }
}
