package com.tzj.view.listener;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.tzj.view.recyclerview.R;


/**
 * 防止多次点击
 */
public abstract class NoDoubleOnClickListener implements View.OnClickListener,View.OnLongClickListener {
    public static int DELAY = 350;
    private static long lastClickTime;
    @Override
    public void onClick(View view) {
        if (notAble()){
            return;
        }
        Integer i = (Integer) view.getTag(R.id.item_index_tag);
        if (i == null) {
            i = -1;
        }
        onMyClick(view,i);
    }

    @Override
    public boolean onLongClick(View view) {
        Integer i = (Integer) view.getTag(R.id.item_index_tag);
        if (i != null) {
            onMyClick(view,i);
            return true;
        }else{
            return false;
        }
    }

    /**
     * @param view
     * @param index v.getTag(R.id.item_index_tag)
     */
    public void onMyClick(View view,int index){
        onMyClick(view);
    }

    public abstract void onMyClick(View view);
    /**
     * 是否可以点击
     */
    public static boolean notAble(){
        return notAble(DELAY);
    }

    /**
     * @param delay　上次点击后延迟的时间
     * @return 不可以点击
     */
    public static synchronized boolean notAble(long delay){
        long l = SystemClock.elapsedRealtime();
        if (lastClickTime > l){
            //防止时间的改变导致长时间不能点击
            lastClickTime = l - delay;
        }
        if (l - lastClickTime <= delay){
            lastClickTime = l;
            Log.w("onClick","点击太快："+lastClickTime);
            return true;
        }
        lastClickTime = l;
        return false;
    }

    /**
     * @param time　未来多长时间不可以点击
     */
    public static void setNotAble(int time){
        lastClickTime = SystemClock.elapsedRealtime()+time;
    }
}
