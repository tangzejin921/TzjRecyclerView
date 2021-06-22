package com.tzj.view.recyclerview;

import com.tzj.view.recyclerview.holder.TzjViewHolder;

public interface IViewType {
    /**
     * 这里直接给 布局
     * 注意：一个布局对应的类不能重复。
     */
    int type();

    /**
     * 这里提供布局对应的 Holder
     */
    default Class<? extends TzjViewHolder> holder() {
        return TzjViewHolder.class;
    }
}