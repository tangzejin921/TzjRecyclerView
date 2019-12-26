package com.suning.wl.sh.view.recycler;

import com.suning.wl.sh.view.recycler.holder.WLViewHolder;

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