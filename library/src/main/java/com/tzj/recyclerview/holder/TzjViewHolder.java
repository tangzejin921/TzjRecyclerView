package com.tzj.recyclerview.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.daimajia.swipe.SwipeLayout;
import com.tzj.recyclerview.R;
import com.tzj.recyclerview.adapter.TzjAdapter;

public class TzjViewHolder<D> extends RecyclerView.ViewHolder {
    protected View.OnClickListener listener;
    protected SwipeLayout swipeLayout;

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void setSwipeLayout(SwipeLayout swipeLayout) {
        this.swipeLayout = swipeLayout;
    }

    public SwipeLayout getSwipeLayout() {
        return swipeLayout;
    }

    public TzjViewHolder(View itemView) {
        super(itemView);
    }

    public void onBind(TzjAdapter adapter, D d, int position) {

    }

    public <T extends View> T bind(int r) {
        return itemView.findViewById(r);
    }

    public void setOnClickListener(View view, int position) {
        view.setTag(R.id.item_index_tag, position);
        view.setOnClickListener(listener);
    }

}
