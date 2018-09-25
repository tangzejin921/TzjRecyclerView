package com.tzj.recyclerview.demo;

import android.view.View;

import com.tzj.recyclerview.adapter.TzjAdapter;
import com.tzj.recyclerview.holder.TzjViewHolder;

public class Demo2Holder extends TzjViewHolder<Demo2Entity>{
    public Demo2Holder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBind(final TzjAdapter adapter, Demo2Entity demo2Entity, int position) {
        setOnClickListener(bind(R.id.swipeMenu),position);
    }

}
