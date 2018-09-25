package com.tzj.recyclerview.entity;

import com.tzj.ISwipeViewType;
import com.tzj.recyclerview.holder.TzjViewHolder;

public class DefaultViewType<T> implements ISwipeViewType {
    private T obj;
    private int type;
    private int swipeId;
    private Class<? extends TzjViewHolder> clzz;

    public DefaultViewType(T obj) {
        this.obj = obj;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setSwipeId(int swipeId) {
        this.swipeId = swipeId;
    }

    public void setClzz(Class<? extends TzjViewHolder> clzz) {
        this.clzz = clzz;
    }

    @Override
    public int type() {
        return type;
    }

    @Override
    public Class<? extends TzjViewHolder> holder() {
        return clzz;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return swipeId;
    }
}
