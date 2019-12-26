package com.suning.wl.sh.view.recycler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.suning.wl.sh.view.recycler.layoutmanager.ILayoutManager;
import com.suning.wl.sh.view.recycler.layoutmanager.Span;


/**
 * 间隔
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;
    private ILayoutManager layoutManager = null;
    private boolean leftRight;
    private boolean topBottom = true;

    public DividerItemDecoration(Context ctx, int orientation) {
        TypedArray a = ctx.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    protected void setDrawable(@NonNull Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null.");
        }
        mDivider = drawable;
    }

    public Drawable getDrawable() {
        return mDivider;
    }

    protected void setLayoutManager(ILayoutManager layoutManager) {
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



    /**
     *
     */
    public DividerItemDecoration setDivider(RecyclerView view,float px) {
        return setDivider(view,px, android.R.color.transparent);
    }

    public DividerItemDecoration setDivider(RecyclerView view,final float px, int color) {
        view.removeItemDecoration(this);
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
        setDrawable(colorDrawable);
        view.addItemDecoration(this);
        view.setBackgroundColor(color);
        return this;
    }

    public void divider(RecyclerView view) {
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();
        if (layoutManager instanceof ILayoutManager){
            setLayoutManager((ILayoutManager)layoutManager);
        }
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
