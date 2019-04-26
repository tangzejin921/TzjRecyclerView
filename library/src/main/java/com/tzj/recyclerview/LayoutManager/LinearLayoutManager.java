package com.tzj.recyclerview.LayoutManager;

import android.content.Context;

/**
 * Created by tzj on 2018/5/30.
 */
public class LinearLayoutManager extends android.support.v7.widget.LinearLayoutManager implements ILayoutManager {
    public LinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public int getSpanCount() {
        return 1;
    }

    @Override
    public void setSpanCount(int spanCount) {
    }

    @Override
    public Span getSpan(int count, int index) {
        return new Span(count, index, index / getSpanCount(), index % getSpanCount(), getSpanCount(), getSpanCount(), null);
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
