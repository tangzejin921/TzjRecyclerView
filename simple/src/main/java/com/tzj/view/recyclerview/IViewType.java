package com.tzj.view.recyclerview;

import com.tzj.view.recyclerview.holder.TzjViewHolder;

public interface IViewType {
    /**
     * 这里直接给 布局
     */
    int type();

    /**
     * 这里提供布局对应的 Holder
     */
    Class<? extends TzjViewHolder> holder();
}