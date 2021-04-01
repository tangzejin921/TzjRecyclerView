package com.tzj.view.recyclerview;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;


import com.tzj.view.recyclerview.adapter.BaseDelegate;
import com.tzj.view.recyclerview.adapter.TzjAdapter;

import java.util.Collections;
import java.util.List;

/**
 * RecyclerView 的交换
 *  ItemTouchHelper itemTouchClearHelper = new ItemTouchHelper(new DrawCallBack());
 *  itemTouchClearHelper.attachToRecyclerView(recyclerView);
 */
public class DrawCallBack extends ItemTouchHelper.Callback {
    /**
     * 不同类型的View是否可以交换
     */
    private boolean diffTypeMoveAbule = true;

    /**
     * @param diffTypeMoveAbule 不同类型的View是否可以交换
     */
    public DrawCallBack setDiffTypeMoveAbule(boolean diffTypeMoveAbule) {
        this.diffTypeMoveAbule = diffTypeMoveAbule;
        return this;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;//拖动
        int swipeFlags = 0;//滑动
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.START | ItemTouchHelper.END;
        } else {
            if (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.VERTICAL) {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            } else {
                dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            }
        }
        return makeMovementFlags(dragFlags, swipeFlags); //该方法指定可进行的操作
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (diffTypeMoveAbule || viewHolder.getItemViewType() == target.getItemViewType()) {
            return true;
        }
        return false;
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof BaseDelegate){
            adapter = ((BaseDelegate)adapter).getAdapter();
        }
        if (adapter instanceof TzjAdapter) {
            TzjAdapter wlAdapter = ((TzjAdapter) adapter);
            //当发生交换时不能用tag方式取index，要用 getAdapterPosition()
            wlAdapter.tagIndex = false;
            List list = wlAdapter.getList();
            Collections.swap(list, viewHolder.getAdapterPosition(), target.getAdapterPosition());
            adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            //此时的itemView的tag是准确的
            viewHolder.itemView.setTag(R.id.item_index_tag,toPos);
            target.itemView.setTag(R.id.item_index_tag,fromPos);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            if (isCurrentlyActive) {//选中
                viewHolder.itemView.setScaleX(0.9f);
                viewHolder.itemView.setScaleY(0.9f);
            } else {//松开
                viewHolder.itemView.setScaleX(1f);
                viewHolder.itemView.setScaleY(1f);
            }
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setScaleX(1f);
        viewHolder.itemView.setScaleY(1f);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }
}
