package com.tzj.view.recyclerview;

import com.tzj.view.recyclerview.holder.WLViewHolder;
import com.tzj.view.recyclerview.layoutmanager.GridLayoutManager;

/**
 */
public class DefaultViewType implements IViewType, GridLayoutManager.SpanSize {

    @Override
    public int type() {
        return R.layout.view_view_type;
    }

    @Override
    public Class<? extends WLViewHolder> holder() {
        return WLViewHolder.class;
    }

    @Override
    public int getSpanSize() {
        return 0;
    }
}
