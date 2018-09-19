package com.tzj.recyclerview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.tzj.recyclerview.LayoutManager.GridLayoutManager;
import com.tzj.recyclerview.LayoutManager.ILayoutManager;
import com.tzj.recyclerview.LayoutManager.LinearLayoutManager;
import com.tzj.recyclerview.LayoutManager.StaggeredGridLayoutManager;
import com.tzj.recyclerview.adapter.AdapterDelegate;
import com.tzj.recyclerview.adapter.TzjAdapter;

import java.util.List;

public class TzjRecyclerView extends RecyclerView{

    public TzjRecyclerView(Context context) {
        super(context);
        init();
    }
    public TzjRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public TzjRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    private void init() {
    }

    //===================================================
    private DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), RecyclerView.VERTICAL);
    public void setLineLayoutManager() {
        setLineLayoutManager(VERTICAL);
    }
    /**
     * 线性布局
     */
    public void setLineLayoutManager(int orientation) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(orientation);
        setLayoutManager(layoutManager);
    }
    /**
     * 网格布局
     */
    public void setGridLayoutManager(int spanCount) {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount,false);
        setLayoutManager(layoutManager);
    }

    /**
     * 网格布局
     * @param spanCount
     * @param canSpan 为了性能考虑
     */
    public void setGridLayoutManager(int spanCount,boolean canSpan) {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount,canSpan);
        setLayoutManager(layoutManager);
    }
    /**
     * 瀑布流布局(应当设置 setDivider(true,？) 不然布局会有一点问题)
     */
    public void setStaggeredGridLayoutManager(int spanCount, int orientation) {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spanCount, orientation);
        layoutManager.setOrientation(orientation);
        setLayoutManager(layoutManager);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (layout instanceof ILayoutManager) {
            dividerItemDecoration.setLayoutManager((ILayoutManager) layout);
            super.setLayoutManager(layout);
        } else {
            throw new RuntimeException("请用 ILayoutManager");
        }
        if (getAdapter()==null){
            setAdapter(new AdapterDelegate());
        }
    }

    public void setDivider(boolean leftRight,boolean topBottom){
        dividerItemDecoration.setDivider(leftRight,topBottom);
        divider(leftRight,topBottom);
    }
    /**
     *
     */
    public void setDivider(float px) {
        setDivider(px,android.R.color.transparent);
    }
    public void setDivider(final float px,int color) {
        removeItemDecoration(dividerItemDecoration);
        ColorDrawable colorDrawable = new ColorDrawable(color) {
            private int h = (int) px;
            @Override
            public int getIntrinsicHeight() {
                return h;
            }
            @Override
            public int getIntrinsicWidth() {
                return getIntrinsicHeight();
            }
        };
        dividerItemDecoration.setDrawable(colorDrawable);
        addItemDecoration(dividerItemDecoration);
        setBackgroundColor(color);
        divider(dividerItemDecoration.isLeftRight(),dividerItemDecoration.isTopBottom());
    }

    private void divider(boolean leftRight,boolean topBottom){
        Drawable drawable = dividerItemDecoration.getDrawable();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        int l=0;
        int t=0;
        int r=0;
        int b=0;
        if (intrinsicHeight>0){
            if (leftRight){
                l=intrinsicHeight/2;
                r = intrinsicHeight/2;
            }
            if (topBottom){
                t=intrinsicHeight/2;
                b=intrinsicHeight/2;
            }
        }
        setClipToPadding(false);
        setPadding(l,t,r,b);
    }


    //=========================adapter 的方法============================
    public void notifyDataSetChanged() {
        getAdapter().notifyDataSetChanged();
    }
    public void setItemClickListener(TzjAdapter.OnItemClickListener itemClickListener) {
        Adapter adapter = getAdapter();
        if (adapter instanceof AdapterDelegate){
            ((AdapterDelegate) adapter).getAdapter().setItemClickListener(itemClickListener);
        }
    }
    public void setClickListener(TzjAdapter.OnClickIndexListener clickListener) {
        Adapter adapter = getAdapter();
        if (adapter instanceof AdapterDelegate){
            ((AdapterDelegate) adapter).getAdapter().setClickListener(clickListener);
        }
    }
    public void setList(List<? extends IViewType> list) {
        Adapter adapter = getAdapter();
        if (adapter instanceof AdapterDelegate){
            ((AdapterDelegate) adapter).getAdapter().setList(list);
        }
    }
    public void addList(List<? extends IViewType> list) {
        Adapter adapter = getAdapter();
        if (adapter instanceof AdapterDelegate){
            ((AdapterDelegate) adapter).getAdapter().addList(list);
        }
    }
    public void addItem(IViewType item){
        Adapter adapter = getAdapter();
        if (adapter instanceof AdapterDelegate){
            ((AdapterDelegate) adapter).getAdapter().addItem(item);
        }
    }
}
