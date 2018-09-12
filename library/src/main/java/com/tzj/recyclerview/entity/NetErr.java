package com.tzj.recyclerview.entity;

import com.tzj.recyclerview.IViewType;
import com.tzj.recyclerview.R;
import com.tzj.recyclerview.holder.NetErrHolder;
import com.tzj.recyclerview.holder.TzjViewHolder;

public class NetErr implements IViewType{

    @Override
    public int type() {
        return R.layout.view_net_err;
    }

    @Override
    public Class<? extends TzjViewHolder> holder() {
        return NetErrHolder.class;
    }
}
