package com.tzj.view.recyclerview;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.tzj.view.recyclerview.layoutmanager.ILayoutManager;
import com.tzj.view.recyclerview.layoutmanager.Span;


/**
 * 间隔
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private ILayoutManager layoutManager = null;
    private DrawableProx mDivider;
    private final Rect mBounds = new Rect();
    /**
     * 如果是Bitmap则无效
     */
    private boolean leftRight;
    /**
     * 如果是Bitmap则表示是否绘制最后一个间隔
     */
    private boolean topBottom = true;

    public DividerItemDecoration() {
    }

    /**
     * 获取xml数据
     */
    public void setXml(TypedArray a){
        int dividerHeight = (int) a.getDimension(R.styleable.RecyclerView_android_dividerHeight,0);
        leftRight = a.getBoolean(R.styleable.RecyclerView_paddingLeftRight,false);
        topBottom = a.getBoolean(R.styleable.RecyclerView_paddingTopBottom,true);
        Drawable tag = a.getDrawable(R.styleable.RecyclerView_listDivider);
        mDivider = new DrawableProx(tag);
        mDivider.setDividerHeight(dividerHeight);
    }

    public DividerItemDecoration setColor(@ColorInt int color) {
        if (mDivider.isColorDrawable()){
            mDivider.setColor(color);
        }
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
        if (mDivider == null){
            throw new RuntimeException("没有从xml里获取到Drawable");
        }
        mDivider.setDividerHeight(mDividerHeight);
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
        if (layoutManager == null) {
            super.onDraw(c,parent,state);
            return;
        }
        //只支持Bitmap
        if (mDivider == null || mDivider.isColorDrawable()){
            super.onDraw(c,parent,state);
            return;
        }
        //只支持 LinearLayoutManager
        if (!(layoutManager instanceof LinearLayoutManager)){
            super.onDraw(c,parent,state);
            return;
        }
        if (layoutManager.getOrientation() == RecyclerView.VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    /**
     * 只支持 LinearLayoutManager
     */
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int left;
        final int right;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (true || parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

//        final int Offset = mDivider.getIntrinsicWidth() / 2;
//        if (leftRight){
//            left += Offset;
//            right -= Offset;
//        }
        final int childCount = parent.getChildCount()-(topBottom?0:1);
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            final int top = bottom - mDivider.getIntrinsic(layoutManager.getOrientation());
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    /**
     * 只支持 LinearLayoutManager
     */
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int top;
        final int bottom;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (true || parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top,
                    parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        final int childCount = parent.getChildCount()-(topBottom?0:1);
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int right = mBounds.right + Math.round(child.getTranslationX());
            final int left = right - mDivider.getIntrinsic(layoutManager.getOrientation());
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }
        if (layoutManager == null) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }
        Integer i = parent.getChildAdapterPosition(view);
        RecyclerView.Adapter adapter = parent.getAdapter();

        if (mDivider.isColorDrawable()){
            getItemOffsets4Color(adapter,i,outRect, view, parent, state);
        }else{
            getItemOffsets4Bitmap(adapter,i,outRect, view, parent, state);
        }
    }

    private void getItemOffsets4Color(RecyclerView.Adapter adapter,Integer i,Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
        int left = mDivider.getIntrinsic(layoutManager.getOrientation()) / 2;
        int top = left;
        int right = left;
        int bottom = left;
        if (i != null) {
            Span span = layoutManager.getSpan(adapter.getItemCount(), i);
            boolean isFirstL = span.isFirstX();
            boolean isFirstH = span.isFirstY();
            boolean isLastL = span.isLastX();
            boolean isLastH = span.isLastY();
            Log.e("debug",String.format("%d: %s %s %s %s",i,isFirstL,isFirstH,isLastL,isLastH));
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
                if (isFirstH && !leftRight) {
                    left = 0;
                }
                if (!topBottom && layoutManager instanceof GridLayoutManager){
                    // TODO: 当 横向 GridLayoutManager 时并且无 topBottom时界面有问题
                } else {
                    if (isFirstL && !topBottom) {
                        top = 0;
                    }
                    if (isLastL && !topBottom) {
                        bottom = 0;
                    }
                }
                if (isLastH && !leftRight) {
                    right = 0;
                }
            }
        }
        Log.e("debug",String.format("%d: %d %d %d %d",i,left,top,right,bottom));
        //TODO 当 leftRight为false 时会导致view  大小不一样
        outRect.set(left, top, right, bottom );
    }

    /**
     * 只支持LinearLayoutManager
     */
    private void getItemOffsets4Bitmap(RecyclerView.Adapter adapter,Integer i,Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
        //只支持LinearLayoutManager
        if (!(layoutManager instanceof LinearLayoutManager)){
            return;
        }
        ;
        int left = mDivider.getIntrinsic(layoutManager.getOrientation());
        int top = left;
        int right = left;
        int bottom = left;
        if (i != null) {
            Span span = layoutManager.getSpan(adapter.getItemCount(), i);
//            boolean isFirstL = span.isFirstX();
//            boolean isFirstH = span.isFirstY();
            boolean isLastL = span.isLastX();
            boolean isLastH = span.isLastY();
            if (this.layoutManager.getOrientation() == RecyclerView.VERTICAL) {
                left = 0;
                right = 0;
                top = 0;
                if (isLastL && !topBottom) {
                    bottom = 0;
                }
            } else {
                top = 0;
                bottom = 0;
                left = 0;
                if (isLastH && !leftRight) {
                    right = 0;
                }
            }
        }
        //TODO 当 leftRight为false 时会导致view  大小不一样
        outRect.set(left, top, right, bottom);
    }
    /**
     * 设置　Divider 并且设置View的padding
     */
    public void divider(RecyclerView view) {
        view.removeItemDecoration(this);
        view.addItemDecoration(this);
        if (mDivider.isColorDrawable()){
            divider4Color(view);
        }else{
            divider4Bitmap(view);
        }
    }

    /**
     * RecyclerView 4周加 dividerHeight (1/2)大小的padding
     */
    private void divider4Color(RecyclerView view){
        view.setBackgroundColor(mDivider.getColor());
        Drawable drawable = getDrawable();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        int l = view.getPaddingLeft();
        int t = view.getPaddingTop();
        int r = view.getPaddingRight();
        int b = view.getPaddingBottom();
        if (intrinsicHeight > 0) {
            if (leftRight) {
                l += intrinsicHeight / 2;
                r += intrinsicHeight / 2;
            }
            if (topBottom) {
                t += intrinsicHeight / 2;
                b += intrinsicHeight / 2;
            }
        }
        view.setClipToPadding(false);
        view.setPadding(l, t, r, b);
    }

    private void divider4Bitmap(RecyclerView view){
        view.setClipToPadding(false);
    }

    private static class DrawableProx extends Drawable {
        private int mDividerHeight = 0;
        private Drawable mTag;
        /**
         * 如果是color
         * 1.不使用draw,而是用背景。
         * 2.RecyclerView四周会使用padding
         *
         * 如果是图片
         * 1.使用draw方式。
         * 2.只支持LineLayout布局
         * 3.上下将不使用padding
         * todo 间隔在边界时可能不会显示出来
         */
        private boolean isColorDrawable;

        DrawableProx(Drawable tag) {
            if (tag == null){
                tag = new ColorDrawable(0x00000000);
            }
            this.mTag = tag;
            if (mTag instanceof ColorDrawable) {
                isColorDrawable = true;
            }
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            if (mTag != null) {
                mTag.draw(canvas);
            }
        }

        @Override
        public void setAlpha(int i) {
            if (mTag != null) {
                mTag.setAlpha(i);
            }
        }

        @Override
        public void setBounds(@NonNull Rect bounds) {
            if (mTag != null) {
                mTag.setBounds(bounds);
            }
        }

        @Override
        public void setBounds(int left, int top, int right, int bottom) {
            if (mTag != null) {
                mTag.setBounds(left, top, right, bottom);
            }
        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {
            if (mTag != null) {
                mTag.setColorFilter(colorFilter);
            }
        }

        @Override
        public int getOpacity() {
            if (mTag != null) {
                return mTag.getOpacity();
            } else {
                return PixelFormat.UNKNOWN;
            }
        }

        public int getIntrinsic(int orientation){
            if (orientation == LinearLayoutManager.VERTICAL){
                return getIntrinsicHeight();
            }else{
                return getIntrinsicWidth();
            }
        }

        @Override
        public int getIntrinsicWidth() {
            if (isColorDrawable) {
                return mDividerHeight;
            } else if (mTag != null) {
                return mTag.getIntrinsicWidth();
            } else {
                return super.getIntrinsicWidth();
            }
        }

        @Override
        public int getIntrinsicHeight() {
            if (isColorDrawable) {
                return mDividerHeight;
            } else if (mTag != null) {
                return mTag.getIntrinsicHeight();
            } else {
                return super.getIntrinsicHeight();
            }
        }

        public boolean isColorDrawable() {
            return isColorDrawable;
        }

        public void setColor(int color) {
            if (isColorDrawable) {
                ((ColorDrawable) mTag).setColor(color);
            }
        }

        public int getColor() {
            if (isColorDrawable) {
                return ((ColorDrawable) mTag).getColor();
            }
            return 0x00000000;
        }

        public void setDividerHeight(int dividerHeight){
            this.mDividerHeight = dividerHeight;
        }
    }
}
