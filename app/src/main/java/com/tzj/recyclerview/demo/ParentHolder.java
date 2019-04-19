package com.tzj.recyclerview.demo;

import android.view.View;

import com.tzj.recyclerview.TzjRecyclerView;
import com.tzj.recyclerview.adapter.TzjAdapter;
import com.tzj.recyclerview.holder.TzjViewHolder;

/**
 * Copyright © 2019 健康无忧网络科技有限公司<br>
 * Author:      唐泽金 tangzejin921@qq.com<br>
 * Version:     1.0.0<br>
 * Date:        2019/4/19 11:10<br>
 * Description:
 */
public class ParentHolder extends TzjViewHolder<Object> {

    private TzjRecyclerView recyclerView;

    public ParentHolder(final View itemView) {
        super(itemView);
        recyclerView = bind(R.id.recyclerView);
        recyclerView.setGridLayoutManager(3);
        recyclerView.setViewType(android.R.layout.simple_list_item_1, TzjViewHolder.class);

        recyclerView.addItem("");
        recyclerView.addItem("");
        recyclerView.addItem("");
        recyclerView.addItem("");
        recyclerView.addItem("");
    }

    @Override
    public void onBind(TzjAdapter adapter, Object o, int position) {
        super.onBind(adapter, o, position);
        recyclerView.notifyDataSetChanged();
    }
}
