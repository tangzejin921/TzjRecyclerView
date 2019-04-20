package com.tzj.recyclerview.demo2;

import android.view.View;
import android.widget.TextView;

import com.tzj.recyclerview.adapter.TzjAdapter;
import com.tzj.recyclerview.demo.R;
import com.tzj.recyclerview.holder.TzjViewHolder;

/**
 * Copyright © 2019 健康无忧网络科技有限公司<br>
 * Author:      唐泽金 tangzejin921@qq.com<br>
 * Version:     1.0.0<br>
 * Date:        2019/4/20 17:40<br>
 * Description:
 */
public class ChildHolder extends TzjViewHolder<String> {

    private TextView tv;
    public ChildHolder(View itemView) {
        super(itemView);
        tv = bind(R.id.textView);
    }

    @Override
    public void onBind(TzjAdapter adapter, String s, int position) {
        super.onBind(adapter, s, position);
        tv.setText(s);
    }

}
