package com.tzj.view.recyclerview.layoutmanager;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

import com.tzj.view.recyclerview.DividerItemDecoration;
import com.tzj.view.recyclerview.R;

/**
 *
 */
public class LinearLayoutManager extends androidx.recyclerview.widget.LinearLayoutManager implements ILayoutManager {
    private DividerItemDecoration divider = new DividerItemDecoration();
    public LinearLayoutManager(Context context) {
        super(context);
        divider.setLayoutManager(this);
    }

    public LinearLayoutManager(Context context, @RecyclerView.Orientation int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        divider.setLayoutManager(this);
    }

    public LinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        divider.setLayoutManager(this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerView,defStyleAttr, defStyleRes);
        divider.setXml(a);
        a.recycle();
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
