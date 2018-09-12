package com.tzj.recyclerview.demo;

import com.tzj.recyclerview.IViewType;
import com.tzj.recyclerview.holder.TzjViewHolder;

public class DemoEntity implements IViewType{
    public String s;

    public DemoEntity(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return "DemoEntity{" +
                "s='" + s + '\'' +
                '}';
    }

    @Override
    public int type() {
        return R.layout.item_main;
    }

    @Override
    public Class<? extends TzjViewHolder> holder() {
        return DemoHolder.class;
    }
}
