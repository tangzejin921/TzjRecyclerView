package com.tzj.recyclerview.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tzj.recyclerview.IViewType;
import com.tzj.recyclerview.TzjRecyclerView;
import com.tzj.recyclerview.adapter.AdapterDelegate;
import com.tzj.recyclerview.adapter.TzjAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TzjAdapter.OnItemClickListener{


    private List<IViewType> list = new ArrayList<>();
    private TzjRecyclerView mRecyclerView;
    private AdapterDelegate adapter;

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
            list.add(new DemoEntity("kl"+ i));
        }
        list.add(new Demo2Entity("shfdf"));

        ;
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setGridLayoutManager(5,true);
        mRecyclerView.setDivider(50,0xFFFF0000);
        mRecyclerView.setDivider(true,true);
        mRecyclerView.setAdapter(adapter = new AdapterDelegate());
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.getAdapter().setList(list);
                        adapter.notifyDataSetChanged();
                    }
                },5000);
            }
        },5000);
        adapter.getAdapter().setItemClickListener(this);
        adapter.getAdapter().setClickListener(new TzjAdapter.OnClickIndexListener() {
            @Override
            public void onClick(View v, int index) {
                Toast.makeText(v.getContext(),list.get(index).toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(TzjAdapter adapter, View v, int index, Object obj) {
        Toast.makeText(this,obj.toString(),Toast.LENGTH_LONG).show();
    }
}
