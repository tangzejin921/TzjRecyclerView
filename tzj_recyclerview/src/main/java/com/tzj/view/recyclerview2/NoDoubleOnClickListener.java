package com.tzj.view.recyclerview2;

import android.view.View;


/**
 * 防止多次点击
 */
public abstract class NoDoubleOnClickListener implements View.OnClickListener,View.OnLongClickListener {
    protected long preTime;

    @Override
    public void onClick(View v) {
        long time = System.currentTimeMillis();
        if (time - preTime > 500) {
            preTime = time;
            Integer i = (Integer) v.getTag(R.id.item_index_tag);
            if (i == null) {
                i = -1;
            }
            onMyClick(v,i);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        Integer i = (Integer) v.getTag(R.id.item_index_tag);
        if (i != null) {
            onMyClick(v,i);
            return true;
        }else{
            return false;
        }
    }

    public abstract void onMyClick(View v, int index);
}
