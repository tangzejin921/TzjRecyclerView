package com.tzj.recyclerview;

import com.tzj.recyclerview.holder.TzjViewHolder;

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
