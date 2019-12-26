package com.tzj.view.recyclerview2.recycler;

import com.tzj.view.recyclerview2.recycler.holder.WLViewHolder;

public interface IViewType {
    /**
     * 这里直接给 布局
     */
    int type();

    /**
     * 这里提供布局对应的 Holder
     */
    Class<? extends WLViewHolder> holder();
}