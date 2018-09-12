package com.tzj.recyclerview.demo;

import com.tzj.recyclerview.IViewType;
import com.tzj.recyclerview.LayoutManager.GridLayoutManager;
import com.tzj.recyclerview.holder.TzjViewHolder;

public class Demo2Entity implements IViewType,GridLayoutManager.SpanSize{
    public String s;

    public Demo2Entity(String s) {
        this.s = s;
    }

    @Override
    public int type() {
        return R.layout.item_demo2;
    }

    @Override
    public Class<? extends TzjViewHolder> holder() {
        return TzjViewHolder.class;
    }

    @Override
    public int getSpanSize() {
        return 4;
    }
}
