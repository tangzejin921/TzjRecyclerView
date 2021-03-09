package com.tzj.view.recyclerview;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tzj.view.recyclerview.layoutmanager.ILayoutManager;
import com.tzj.view.recyclerview.layoutmanager.Span;


/**
 * 间隔
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private ILayoutManager layoutManager = null;
    private int mDividerHeight = 0;
    private ColorDrawable mDivider = new ColorDrawable(android.R.color.transparent) {

        @Override
        public int getIntrinsicHeight() {
            return mDividerHeight;
        }

        @Override
        public int getIntrinsicWidth() {
            return getIntrinsicHeight();
        }
    };
    private boolean leftRight;
    private boolean topBottom = true;

    public DividerItemDecoration() {
    }

    /**
     * 获取xml数据
     */
    public void setXml(TypedArray a){
        mDividerHeight = (int) a.getDimension(R.styleable.RecyclerView_android_dividerHeight,0);
        leftRight = a.getBoolean(R.styleable.RecyclerView_paddingLeftRight,false);
        topBottom = a.getBoolean(R.styleable.RecyclerView_paddingTopBottom,true);
        mDivider.setColor(a.getColor(R.styleable.RecyclerView_android_listDivider,0x00000000));
    }

    public DividerItemDecoration setColor(@ColorInt int color) {
        mDivider.setColor(color);
        return this;
    }

    public Drawable getDrawable() {
        return mDivider;
    }

    public void setLayoutManager(ILayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    /**
     * 是否留边
     * @param leftRight 默认为false    为true时RecyclerView 的padding 将不起作用
     * @param topBottom 默认为true     为true时RecyclerView 的padding 将不起作用
     */
    public DividerItemDecoration setDivider(boolean leftRight, boolean topBottom) {
        this.leftRight = leftRight;
        this.topBottom = topBottom;
        return this;
    }

    public DividerItemDecoration setmDividerHeight(int mDividerHeight) {
        this.mDividerHeight = mDividerHeight;
        return this;
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

        int left = mDivider.getIntrinsicWidth() / 2;
        int top = mDivider.getIntrinsicWidth() / 2;
        int right = mDivider.getIntrinsicWidth() / 2;
        int bottom = mDivider.getIntrinsicWidth() / 2;
        if (i != null) {
            Span span = layoutManager.getSpan(adapter.getItemCount(), i);
            boolean isFirstL = span.isFirstX();
            boolean isFirstH = span.isFirstY();
            boolean isLastL = span.isLastX();
            boolean isLastH = span.isLastY();
            if (this.layoutManager.getOrientation() == RecyclerView.VERTICAL) {
                if (isFirstL && !leftRight) {
                    left = 0;
                }
                if (isFirstH && !topBottom) {
                    top = 0;
                }
                if (isLastH && !topBottom) {
                    bottom = 0;
                }
                if (isLastL && !leftRight) {
                    right = 0;
                }
            } else {
                if (isFirstL && !leftRight) {
                    left = 0;
                }
                if (isFirstH && !topBottom) {
                    top = 0;
                }
                if (isLastH && !topBottom) {
                    bottom = 0;
                }
                if (isLastL && !leftRight) {
                    right = 0;
                }
            }
        }
        //TODO 当 leftRight为false 时会导致view  大小不一样
        outRect.set(left, top, right, bottom);
    }

    public void divider(RecyclerView view) {
        view.removeItemDecoration(this);
        view.addItemDecoration(this);
        view.setBackgroundColor(mDivider.getColor());
        Drawable drawable = getDrawable();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        int l = view.getPaddingLeft();
        int t = view.getPaddingTop();
        int r = view.getPaddingRight();
        int b = view.getPaddingBottom();
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
        view.setClipToPadding(false);
        view.setPadding(l, t, r, b);
    }
}
