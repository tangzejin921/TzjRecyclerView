package com.suning.wl.sh.view.recycler.layoutmanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.WLSpanSizeLookup;
import android.util.AttributeSet;

/**
 *
 */
public class GridLayoutManager extends android.support.v7.widget.GridLayoutManager implements ILayoutManager {
    private boolean canSpan;

    /**
     * @param context
     * @param spanCount
     * @param canSpan   是否可以设置 SpanSizeLookup,如果span 是一样的请设置为false,减少计算
     */
    public GridLayoutManager(Context context, int spanCount, boolean canSpan) {
        super(context, spanCount);
        this.canSpan = canSpan;
    }

    public GridLayoutManager(Context context, int spanCount, @RecyclerView.Orientation int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public GridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        if (canSpan) {
            super.setSpanSizeLookup(spanSizeLookup);
        }
    }

    @Override
    public Span getSpan(int count, int index) {
        SpanSizeLookup spanSizeLookup = getSpanSizeLookup();
        if (spanSizeLookup instanceof WLSpanSizeLookup) {
            return ((WLSpanSizeLookup) spanSizeLookup).getSpan(count, index, getSpanCount());
        } else {
            int spanCount = getSpanCount();//
            int spanGroupIndex = spanSizeLookup.getSpanGroupIndex(index, spanCount);//第几行
            int spanIndex = spanSizeLookup.getSpanIndex(index, spanCount);//第几个
            int spanSize = spanSizeLookup.getSpanSize(index);//占几个
            return new Span(count, index, spanGroupIndex, spanIndex, getSpanCount(), spanSize, null);
        }
    }

    public interface SpanSize {
        int getSpanSize();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ILayoutManager) {
            ILayoutManager manager = (ILayoutManager) obj;
            return manager.getClass().equals(getClass())
                    && manager.getOrientation() == getOrientation()
                    && manager.getSpanCount() == getSpanCount();
        } else {
            return super.equals(obj);
        }
    }
}
