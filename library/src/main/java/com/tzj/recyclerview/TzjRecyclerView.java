package com.tzj.recyclerview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.implments.SwipeItemMangerImpl2;
import com.daimajia.swipe.interfaces.SwipeAdapterInterface;
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface;
import com.daimajia.swipe.util.Attributes;
import com.tzj.recyclerview.LayoutManager.GridLayoutManager;
import com.tzj.recyclerview.LayoutManager.ILayoutManager;
import com.tzj.recyclerview.LayoutManager.LinearLayoutManager;
import com.tzj.recyclerview.LayoutManager.StaggeredGridLayoutManager;
import com.tzj.recyclerview.adapter.AdapterDelegate;
import com.tzj.recyclerview.adapter.TzjAdapter;
import com.tzj.recyclerview.entity.DefaultViewType;
import com.tzj.recyclerview.entity.Empty;
import com.tzj.recyclerview.holder.TzjViewHolder;

import java.util.List;

public class TzjRecyclerView extends RecyclerView implements SwipeItemMangerInterface {

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

    /**
     * onDetachedFromWindow 时记录的adapter
     */
    private Adapter detachedAdapter;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (detachedAdapter != null && getAdapter() == null) {
            setAdapter(detachedAdapter);
        }
        detachedAdapter = null;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        detachedAdapter = getAdapter();
        setAdapter(null);//这里让 AdapterDelegate 走 onDetachedFromRecyclerView
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
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount, false);
        setLayoutManager(layoutManager);
    }

    /**
     * 网格布局
     *
     * @param spanCount
     * @param canSpan   为了性能考虑
     */
    public void setGridLayoutManager(int spanCount, boolean canSpan) {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount, canSpan);
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
        if (getAdapter() == null) {
            setAdapter(new AdapterDelegate());
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof SwipeAdapterInterface) {
            mItemManger = new SwipeItemMangerImpl2((SwipeAdapterInterface) adapter);
        }
    }

    /**
     * 要在其他setDivider 之前调用
     *
     * @param leftRight 为 true RecyclerView 的padding 将不起作用
     * @param topBottom 为 true RecyclerView 的padding 将不起作用
     */
    public void setDivider(boolean leftRight, boolean topBottom) {
        dividerItemDecoration.setDivider(leftRight, topBottom);
        divider(leftRight, topBottom);
    }

    /**
     *
     */
    public void setDivider(float px) {
        setDivider(px, android.R.color.transparent);
    }

    public void setDivider(final float px, int color) {
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
        divider(dividerItemDecoration.isLeftRight(), dividerItemDecoration.isTopBottom());
    }

    private void divider(boolean leftRight, boolean topBottom) {
        Drawable drawable = dividerItemDecoration.getDrawable();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        int l = getPaddingLeft();
        int t = getPaddingTop();
        int r = getPaddingRight();
        int b = getPaddingBottom();
        if (intrinsicHeight > 0) {
            if (leftRight) {
                l = intrinsicHeight / 2;
                r = intrinsicHeight / 2;
            }
            if (topBottom) {
                t = intrinsicHeight / 2;
                b = intrinsicHeight / 2;
            }
        }
        setClipToPadding(false);
        setPadding(l, t, r, b);
    }


    //=========================adapter 的方法============================
    public void notifyDataSetChanged() {
        Adapter adapter = getAdapter();
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * item 的点击事件
     */
    public void setItemClickListener(TzjAdapter.OnItemClickListener itemClickListener) {
        getTzjAdapter().setItemClickListener(itemClickListener);
    }

    /**
     * item 的长按事件
     */
    public void setItemClickListener(TzjAdapter.OnItemLongClickListener itemClickListener) {
        getTzjAdapter().setItemLongClickListener(itemClickListener);
    }

    /**
     * item 内部的子View 点击事件
     */
    public void setClickListener(TzjAdapter.OnClickIndexListener clickListener) {
        getTzjAdapter().setClickListener(clickListener);
    }

    public void setList(List list) {
        getTzjAdapter().setList(list);
    }

    public void addList(List list) {
        getTzjAdapter().addList(list);
    }

    public void addItem(Object item) {
        getTzjAdapter().addItem(item);
    }

    public Empty getEmpty() {
        Adapter adapter = getAdapter();
        if (adapter instanceof AdapterDelegate) {
            return ((AdapterDelegate) adapter).getEmptyAdapter().getEmpty();
        } else {
            throw new RuntimeException();
        }
    }

    public void setViewType(int r, Class<? extends TzjViewHolder> clzz) {
        setViewType(r, 0, clzz);
    }

    public void setViewType(int r, int swipe, Class<? extends TzjViewHolder> clzz) {
        DefaultViewType viewType = new DefaultViewType(null);
        viewType.setType(r);
        viewType.setSwipeId(swipe);
        viewType.setClzz(clzz);
        getTzjAdapter().setViewType(viewType);
    }

    /**
     * 都到真实的 Adapter
     */
    public TzjAdapter getTzjAdapter(){
        Adapter adapter = getAdapter();
        if (adapter instanceof AdapterDelegate) {
            adapter = ((AdapterDelegate) adapter).getAdapter();
        }
        if (adapter instanceof TzjAdapter){
            return (TzjAdapter) adapter;
        }
        throw new RuntimeException("请用 TzjAdapter");
    }
    //========================= SwipeLayout 相关============================
    private SwipeItemMangerImpl2 mItemManger;

    public SwipeItemMangerImpl2 getItemManger() {
        return mItemManger;
    }

    @Override
    public void openItem(int position) {
        if (mItemManger != null) {
            mItemManger.openItem(position);
        }
    }

    @Override
    public void closeItem(int position) {
        if (mItemManger != null) {
            mItemManger.closeItem(position);
        }
    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {
        if (mItemManger != null) {
            mItemManger.closeAllExcept(layout);
        }
    }

    @Override
    public void closeAllItems() {
        if (mItemManger != null) {
            mItemManger.closeAllItems();
        }
    }

    @Override
    public List<Integer> getOpenItems() {
        if (mItemManger != null) {
            return mItemManger.getOpenItems();
        }
        return null;
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        if (mItemManger != null) {
            return mItemManger.getOpenLayouts();
        }
        return null;
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
        if (mItemManger != null) {
            mItemManger.removeShownLayouts(layout);
        }
    }

    @Override
    public boolean isOpen(int position) {
        if (mItemManger != null) {
            return mItemManger.isOpen(position);
        }
        return false;
    }

    @Override
    public Attributes.Mode getMode() {
        if (mItemManger != null) {
            return mItemManger.getMode();
        }
        return null;
    }

    @Override
    public void setMode(Attributes.Mode mode) {
        if (mItemManger != null) {
            mItemManger.setMode(mode);
        }
    }
}
