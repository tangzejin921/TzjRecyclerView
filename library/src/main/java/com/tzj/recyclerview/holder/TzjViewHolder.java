package com.tzj.recyclerview.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.daimajia.swipe.SwipeLayout;
import com.tzj.recyclerview.R;
import com.tzj.recyclerview.adapter.TzjAdapter;

public class TzjViewHolder<D> extends RecyclerView.ViewHolder {
    protected View.OnClickListener listener;
    protected SwipeLayout swipeLayout;
    /**
     * 是否 binded 了
     */
    public boolean isFirstBinded = false;

    /**
     * view 的点击事件
     */
    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    /**
     * 让 TzjAdapter 调用
     */
    public void setSwipeLayout(SwipeLayout swipeLayout) {
        this.swipeLayout = swipeLayout;
    }

    public SwipeLayout getSwipeLayout() {
        return swipeLayout;
    }

    public TzjViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * 创建 View 时调用，可以初始化
     *构造方法里取不到 TzjAdapter
     */
    public void onCreateView(Context ctx,TzjAdapter adapter,View itemView){

    }
    /**
     *
     */
    public void onBind(TzjAdapter adapter, D d, int position) {

    }

    /**
     * findViewById
     */
    public <T extends View> T bind(int r) {
        return itemView.findViewById(r);
    }

    /**
     * 绑定 OnclickListener
     */
    public void setOnClickListener(View view, int position) {
        view.setTag(R.id.item_index_tag, position);
        view.setOnClickListener(listener);
    }

    /**
     *
     */
    public boolean onClickable(){
        return true;
    }
}
