package com.tzj.listener;

import android.view.View;

/**
 * Created by user on 2016/11/22.
 * 防止多次点击
 */
public abstract class NoDoubleOnClickListener implements View.OnClickListener{
    protected long preTime;
    @Override
    public void onClick(View v) {
        long time = System.currentTimeMillis();
        if (time-preTime>500){
            preTime = time;
            onMyClick(v);
        }
    }

    public abstract void onMyClick(View v);
}
