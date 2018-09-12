package com.tzj.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.tzj.recyclerview.LayoutManager.ILayoutManager;
import com.tzj.recyclerview.LayoutManager.Span;

/**
 * 间隔
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;
    private ILayoutManager layoutManager = null;
    private boolean leftRight;
    private boolean topBottom=true;

    public DividerItemDecoration(Context ctx, int orientation) {
        TypedArray a = ctx.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    public void setDrawable(@NonNull Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null.");
        }
        mDivider = drawable;
    }

    public Drawable getDrawable() {
        return mDivider;
    }

    public void setLayoutManager(ILayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }
    /**
     * 是否留边
     */
    public void setDivider(boolean leftRight,boolean topBottom){
        this.leftRight = leftRight;
        this.topBottom = topBottom;
    }

    public boolean isLeftRight() {
        return leftRight;
    }

    public void setLeftRight(boolean leftRight) {
        this.leftRight = leftRight;
    }

    public boolean isTopBottom() {
        return topBottom;
    }

    public void setTopBottom(boolean topBottom) {
        this.topBottom = topBottom;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //TODO 这里先这样用背景，过度绘制的问题先不管
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }
        if (layoutManager == null) {
            return;
        }
        Integer i = parent.getChildAdapterPosition(view);
        RecyclerView.Adapter adapter = parent.getAdapter();

        int left = mDivider.getIntrinsicWidth()/2;
        int top = mDivider.getIntrinsicWidth()/2;
        int right = mDivider.getIntrinsicWidth()/2;
        int bottom = mDivider.getIntrinsicWidth()/2;
        if (i != null) {
            Span span = layoutManager.getSpan(adapter.getItemCount(), i);
            boolean isFirstL = span.isFirstX();
            boolean isFirstH = span.isFirstY();
            boolean isLastL = span.isLastX();
            boolean isLastH = span.isLastY();
            Log.e("test",isFirstL+"=="+isFirstH+"=="+isLastL+"=="+isLastH);
            if (this.layoutManager.getOrientation() == RecyclerView.VERTICAL) {
                if (isFirstL&&!leftRight) {
                    left = 0;
                }
                if (isFirstH&&!topBottom) {
                    top = 0;
                }
                if (isLastH&&!topBottom) {
                    bottom = 0;
                }
                if (isLastL&&!leftRight) {
                    right = 0;
                }
            } else {
                if (isFirstL&&!leftRight) {
                    left = 0;
                }
                if (isFirstH&&!topBottom) {
                    top = 0;
                }
                if (isLastH&&!topBottom) {
                    bottom = 0;
                }
                if (isLastL&&!leftRight) {
                    right = 0;
                }
            }
        }
        // todo 当 leftRight为false 时会导致view  大小不一样
        outRect.set(left, top, right, bottom);
    }
}
