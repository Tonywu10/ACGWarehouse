package com.example.tonyw.acgwarehouse.entity;

import com.example.tonyw.acgwarehouse.utils.Entity;

/**
 * Created by tonywu10 on 2016/12/13.
 */

public class CollectAnimateEntity extends Entity {
    public CollectAnimateEntity(int thumb,String title,int episode,boolean isUpdate)
    {
        super(thumb,title,episode,isUpdate);
    }

    @Override
    public int getItemType() {
        return COLLECT_ANIMATE_ITEM_TYPE;
    }
}
