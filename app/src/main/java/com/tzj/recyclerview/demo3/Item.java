package com.tzj.recyclerview.demo3;


import com.tzj.recyclerview.IViewType;
import com.tzj.recyclerview.demo.R;
import com.tzj.recyclerview.holder.TzjViewHolder;

public class Item implements IViewType {
    String str;

    public Item(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    @Override
    public int type() {
        return R.layout.item_test;
    }

    @Override
    public Class<? extends TzjViewHolder> holder() {
        return ItemHolder.class;
    }
}
