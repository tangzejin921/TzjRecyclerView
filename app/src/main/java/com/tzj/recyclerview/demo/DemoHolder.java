package com.tzj.recyclerview.demo;

import android.view.View;
import android.widget.TextView;

import com.tzj.recyclerview.adapter.TzjAdapter;
import com.tzj.recyclerview.holder.TzjViewHolder;

public class DemoHolder extends TzjViewHolder<DemoEntity>{

    private TextView textView;
    public DemoHolder(View itemView) {
        super(itemView);
        textView = bind(R.id.textView);
    }

    @Override
    public void onBind(TzjAdapter adapter,DemoEntity o, int position) {
        textView.setText(o.s);
        setOnClickListener(textView,position);
    }
}
