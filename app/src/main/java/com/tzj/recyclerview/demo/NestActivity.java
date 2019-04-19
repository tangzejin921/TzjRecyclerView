package com.tzj.recyclerview.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.tzj.recyclerview.TzjRecyclerView;
import com.tzj.recyclerview.adapter.TzjAdapter;

/**
 * Copyright © 2019 健康无忧网络科技有限公司<br>
 * Author:      唐泽金 tangzejin921@qq.com<br>
 * Version:     1.0.0<br>
 * Date:        2019/4/19 11:05<br>
 * Description: TODO 请输入此类的功能
 */
public class NestActivity extends Activity {

    private TzjRecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nest);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLineLayoutManager();
        recyclerView.setViewType(R.layout.item_parent,ParentHolder.class);
        recyclerView.addItem("");
        recyclerView.addItem("");
        recyclerView.addItem("");
        recyclerView.addItem("");
        recyclerView.addItem("");
        recyclerView.addItem("");
        recyclerView.addItem("");
        recyclerView.addItem("");
        recyclerView.notifyDataSetChanged();
        recyclerView.setItemClickListener(new TzjAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TzjAdapter adapter, View v, int index, Object obj) {
                Log.e("test",index+"");
            }
        });
    }





}
