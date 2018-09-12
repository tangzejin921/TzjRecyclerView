package com.tzj.recyclerview.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tzj.recyclerview.R;

public class TzjViewHolder<D> extends RecyclerView.ViewHolder {
    private View.OnClickListener listener;
    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }
    public TzjViewHolder(View itemView) {
        super(itemView);
    }

    public void onBind(D d,int position){

    }

    public <T extends View> T  bind(int r){
        return itemView.findViewById(r);
    }

    public void setOnClickListener(View view,int position){
        view.setTag(R.id.item_index_tag,position);
        view.setOnClickListener(listener);
    }

}
