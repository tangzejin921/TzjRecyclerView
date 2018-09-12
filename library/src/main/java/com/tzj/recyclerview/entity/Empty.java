package com.tzj.recyclerview.entity;

import com.tzj.recyclerview.IViewType;
import com.tzj.recyclerview.R;
import com.tzj.recyclerview.holder.EmptyHolder;
import com.tzj.recyclerview.holder.TzjViewHolder;

public class Empty implements IViewType {
    private int imageRes = R.drawable.empty;
    private String text = "暂无数据";
    private String hint = "";

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public int type() {
        return R.layout.view_empty;
    }

    @Override
    public Class<? extends TzjViewHolder> holder() {
        return EmptyHolder.class;
    }
}
