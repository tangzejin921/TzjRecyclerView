package com.tzj.view.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


import com.tzj.view.recyclerview.adapter.BaseDelegate;
import com.tzj.view.recyclerview.adapter.TzjAdapter;

import java.lang.ref.WeakReference;

public class TzjRecyclerView extends RecyclerView {

    public TzjRecyclerView(Context context) {
        super(context);
        init(context,null,0);
    }

    public TzjRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public TzjRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs,defStyle);
    }

    protected void init(Context context, @Nullable AttributeSet attrs,int defStyle) {
    }

    /**
     * onDetachedFromWindow 时记录的adapter
     */
    private WeakReference<? extends Adapter> detachedAdapter;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (detachedAdapter != null){
            Adapter adapter = detachedAdapter.get();
            if (adapter != null && getAdapter() == null) {
                setAdapter(adapter);
            }
            detachedAdapter = null;
        } else {
            detachedAdapter = new WeakReference<>(new BaseDelegate());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Adapter adapter = getAdapter();
        if (adapter != null){
            detachedAdapter = new WeakReference<>(adapter);
        }
        //这里让 AdapterDelegate 走 onDetachedFromRecyclerView
        setAdapter(null);
    }

    /**
     * 都到真实的 Adapter
     */
    public TzjAdapter getTzjAdapter() {
        Adapter adapter = getAdapter();
        if (adapter instanceof BaseDelegate) {
            adapter = ((BaseDelegate) adapter).getAdapter();
        }
        if (adapter instanceof TzjAdapter) {
            return (TzjAdapter) adapter;
        }
        throw new RuntimeException("请用 TzjAdapter");
    }
}
