package com.tzj.recyclerview.adapter;

import com.tzj.recyclerview.IViewType;
import com.tzj.recyclerview.R;
import com.tzj.recyclerview.holder.TzjViewHolder;

public class LoadingAdapter extends TzjAdapter{
    public LoadingAdapter() {
        addItem(new IViewType() {
            @Override
            public int type() {
                return R.layout.view_loading;
            }
            @Override
            public Class<? extends TzjViewHolder> holder() {
                return TzjViewHolder.class;
            }
        });
    }
}
