package com.tzj.recyclerview.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.tzj.recyclerview.IViewType;
import com.tzj.recyclerview.TzjRecyclerView;
import com.tzj.recyclerview.adapter.TzjAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TzjAdapter.OnItemClickListener {


    private List<IViewType> list = new ArrayList<>();
    private TzjRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list.add(new DemoEntity("sdf"));
        list.add(new DemoEntity("sdfasg"));
        list.add(new DemoEntity("fdg"));
        list.add(new DemoEntity("sdfa"));
        list.add(new Demo2Entity("dfsg"));
        for (int i = 0; i < 9; i++) {
            list.add(new DemoEntity("kl" + i));
        }
        list.add(new Demo2Entity("shfdf"));

        ;
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setDivider(true, true);
        mRecyclerView.setDivider(50, 0xFFFF0000);
        mRecyclerView.setGridLayoutManager(5, true);

        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.notifyDataSetChanged();
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.setList(list);
                        mRecyclerView.notifyDataSetChanged();
                    }
                }, 5000);
            }
        }, 5000);
        mRecyclerView.setItemClickListener(this);
        mRecyclerView.setClickListener(new TzjAdapter.OnClickIndexListener() {
            @Override
            public void onClick(View v, int index) {
                Toast.makeText(v.getContext(), "====="+list.get(index).toString(), Toast.LENGTH_LONG).show();
                if (v.getId() == R.id.swipeMenu) {
                    list.remove(index);
                    ((SwipeLayout) v.getParent()).close();
                    mRecyclerView.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onItemClick(TzjAdapter adapter, View v, int index, Object obj) {
        Toast.makeText(this, "-----"+obj.toString(), Toast.LENGTH_LONG).show();
//        list.remove(index);
//        adapter.notifyDataSetChanged();
    }
}
