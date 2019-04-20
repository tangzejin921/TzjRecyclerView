package com.tzj.recyclerview.demo2;

import android.view.View;

import com.tzj.recyclerview.TzjRecyclerView;
import com.tzj.recyclerview.adapter.TzjAdapter;
import com.tzj.recyclerview.demo.R;
import com.tzj.recyclerview.holder.TzjViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Copyright © 2019 健康无忧网络科技有限公司<br>
 * Author:      唐泽金 tangzejin921@qq.com<br>
 * Version:     1.0.0<br>
 * Date:        2019/4/19 11:10<br>
 * Description:
 */
public class ParentHolder extends TzjViewHolder<Integer> {

    private TzjRecyclerView recyclerView;
    private List list = new ArrayList();

    public ParentHolder(final View itemView) {
        super(itemView);
        recyclerView = bind(R.id.recyclerView);
        recyclerView.setLayoutFrozen(true);
    }

    @Override
    public void onBind(TzjAdapter adapter, Integer i, int position) {
        super.onBind(adapter, i, position);
        list.clear();
        int count = new Random().nextInt(10);
        for (int j = 0; j < count; j++) {
            list.add(""+i);
        }
        if (recyclerView.getAdapter() == null){
            recyclerView.setGridLayoutManager(3);
            recyclerView.setViewType(R.layout.item_main, ChildHolder.class);
            recyclerView.setList(list);
        }
        recyclerView.notifyDataSetChanged();
    }

}
