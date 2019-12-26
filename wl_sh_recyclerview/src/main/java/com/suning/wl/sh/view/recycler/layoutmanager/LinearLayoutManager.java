package com.suning.wl.sh.view.recycler.layoutmanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 *
 * @author suning
 */
public class LinearLayoutManager extends android.support.v7.widget.LinearLayoutManager implements ILayoutManager {
    public LinearLayoutManager(Context context) {
        super(context);
    }

    public LinearLayoutManager(Context context, @RecyclerView.Orientation int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
