package com.tzj.recyclerview;

import com.tzj.recyclerview.holder.TzjViewHolder;

public interface IViewType {
    int type();
    Class<? extends TzjViewHolder> holder();
}
