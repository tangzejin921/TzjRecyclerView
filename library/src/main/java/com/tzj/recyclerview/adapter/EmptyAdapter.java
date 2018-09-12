package com.tzj.recyclerview.adapter;

import com.tzj.recyclerview.entity.Empty;

public class EmptyAdapter extends TzjAdapter {

    public EmptyAdapter() {
        addItem(new Empty());
    }
    public Empty getEmpty(){
        return getItem(0);
    }
}
