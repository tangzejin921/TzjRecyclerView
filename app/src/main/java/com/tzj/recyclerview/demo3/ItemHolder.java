package com.tzj.recyclerview.demo3;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tzj.recyclerview.adapter.TzjAdapter;
import com.tzj.recyclerview.demo.R;
import com.tzj.recyclerview.holder.TzjViewHolder;

import java.util.Random;

public class ItemHolder extends TzjViewHolder<Item> {
    private TextView tv;
    public ItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onCreateView(Context ctx, TzjAdapter adapter, View itemView) {
        super.onCreateView(ctx, adapter, itemView);
        tv = bind(R.id.text);
    }

    @Override
    public void onBind(TzjAdapter adapter, Item item, int position) {
        super.onBind(adapter, item, position);
        int i = new Random().nextInt(0xffffff);
        int color = i | 0xff000000;
        tv.setBackgroundColor(color);
        tv.setText(item.getStr());
        setOnClickListener(tv,position);
    }


}
