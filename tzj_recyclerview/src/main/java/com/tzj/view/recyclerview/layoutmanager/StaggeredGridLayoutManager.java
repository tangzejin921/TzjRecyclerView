package com.tzj.view.recyclerview.layoutmanager;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.tzj.view.recyclerview.DividerItemDecoration;
import com.tzj.view.recyclerview.R;

/**
 *
 */

public class StaggeredGridLayoutManager extends android.support.v7.widget.StaggeredGridLayoutManager implements ILayoutManager {
    private DividerItemDecoration divider = new DividerItemDecoration();
    public StaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
        divider.setLayoutManager(this);
    }

    public StaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        divider.setLayoutManager(this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerView,defStyleAttr, defStyleRes);
        divider.setXml(a);
        a.recycle();
    }

    @Override
    public Span getSpan(int count, int index) {
        return new Span(count, index, index / getSpanCount(), index % getSpanCount(), getSpanCount(), 1, null);
    }

    @Override
    public DividerItemDecoration getDivider() {
        return divider;
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
