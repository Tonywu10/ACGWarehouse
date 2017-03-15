package com.example.tonyw.acgwarehouse.entity;

import com.example.tonyw.acgwarehouse.utils.Entity;

public class CategoryEntity extends Entity {
    public CategoryEntity(String name)
    {
        super(name);
    }

    @Override
    public int getItemType() {
        return CATEGORY_ITEM_TYPE;
    }
}
