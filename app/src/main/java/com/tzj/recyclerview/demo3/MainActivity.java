package com.tzj.recyclerview.demo3;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchClearHelper;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.tzj.DrawCallBack;
import com.tzj.recyclerview.TzjRecyclerView;
import com.tzj.recyclerview.adapter.TzjAdapter;
import com.tzj.recyclerview.demo.R;


public class MainActivity extends Activity {


    private TzjAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        TzjRecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLineLayoutManager();
        adapter = recyclerView.getTzjAdapter();
        adapter.setClickListener(new TzjAdapter.OnClickIndexListener() {
            @Override
            public void onClick(View v, int index) {
                Item item = adapter.getItem(index);
                Toast.makeText(v.getContext(),"点击了具体的View:"+item.getStr(), Toast.LENGTH_LONG).show();
            }
        });
        adapter.setItemClickListener(new TzjAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TzjAdapter adapter, View v, int index, Object obj) {
                Item item = adapter.getItem(index);
                Toast.makeText(v.getContext(),"点击了item:"+item.getStr(), Toast.LENGTH_LONG).show();
            }
        });
        adapter.setItemLongClickListener(new TzjAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(TzjAdapter adapter, View v, int index, Object obj) {
                Item item = adapter.getItem(index);
                Toast.makeText(v.getContext(),"长按了："+item.getStr(), Toast.LENGTH_LONG).show();
            }
        });

        adapter.addItem(new Item("1"));
        adapter.addItem(new Item("2"));
        adapter.addItem(new Item("3"));
        adapter.addItem(new Item("4"));
        adapter.addItem(new Item("5"));
        adapter.addItem(new Item("6"));
        adapter.addItem(new Item("7"));
        adapter.addItem(new Item("8"));
        adapter.addItem(new Item("9"));
        adapter.addItem(new Item("10"));
        adapter.addItem(new Item("11"));
        adapter.addItem(new Item("12"));
        adapter.addItem(new Item("13"));
        adapter.addItem(new Item("14"));
        adapter.addItem(new Item("15"));
        adapter.addItem(new Item("16"));
        adapter.addItem(new Item("17"));
        adapter.addItem(new Item("18"));
        adapter.addItem(new Item("19"));
        adapter.addItem(new Item("20"));
        adapter.notifyDataSetChanged();


        ItemTouchClearHelper itemTouchClearHelper = new ItemTouchClearHelper(new DrawCallBack());
        itemTouchClearHelper.attachToRecyclerView(recyclerView);


    }


}
