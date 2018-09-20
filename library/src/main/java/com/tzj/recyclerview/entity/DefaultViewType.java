package com.tzj.recyclerview.entity;

import com.tzj.recyclerview.IViewType;
import com.tzj.recyclerview.holder.TzjViewHolder;

public class DefaultViewType<T> implements IViewType {
    private T obj;
    private int type;
    private Class<? extends TzjViewHolder> clzz;

    public DefaultViewType(T obj) {
        this.obj = obj;
    }

    public void setType(int type) {
        this.type = type;
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
}
