package com.tzj.view.recyclerview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tzj.view.recyclerview.IViewType;
import com.tzj.view.recyclerview.holder.TzjViewHolder;
import com.tzj.view.recyclerview.layoutmanager.ILayoutManager;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 精简Delegate,去除空数据，网络变化等Adapter
 */
public class BaseDelegate extends RecyclerView.Adapter {

    protected WeakReference<RecyclerView> mRecyclerView;
    /**
     * 真实数据的 adapter
     */
    private TzjAdapter adapter = new TzjAdapter();
    /**
     * 当前的 Adapter
     */
    protected RecyclerView.Adapter currentAdapter = adapter;

    /**
     * 记录调用了 notifyDatasetChanged
     * 如果 onAttachedToRecyclerView 之前调用，并不会刷新，
     * 所以记录下，等 onAttachedToRecyclerView 后调用刷新
     */
    private boolean notifyDatasetChanged = false;

    /**
     * 调用刷新时，切换 adapter
     * notifyDataChanged
     */
    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            // 要保持这个 在 getItemCount前调用
            changeAdapterAndNotify();
        }
    };
    /**
     * 这里是让 具体的 adapter 可以调用 刷新
     */
    private RecyclerView.AdapterDataObserver adapterObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            changeAdapterAndNotify();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            notifyItemMoved(fromPosition, toPosition);
        }
    };

    public BaseDelegate() {
        /**
         * 这里设置 true，然后 设置 getItemId，
         * 调用 notifyDataChanged 界面上的view 不会刷新
         * 后来发现并不管用，反而会导致拖动交换不停刷新
         */
//        setHasStableIds(true);
//        adapter.setHasStableIds(true);
        adapter.registerAdapterDataObserver(adapterObserver);
    }

    public TzjAdapter getAdapter() {
        return adapter;
    }

    @Override
    public int getItemCount() {
        return currentAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return currentAdapter.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return currentAdapter.getItemId(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return currentAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        notifyDatasetChanged = false;
        currentAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        currentAdapter.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
        return currentAdapter.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        currentAdapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        currentAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
//        registerAdapterDataObserver(observer);
        try {
            adapter.registerAdapterDataObserver(adapterObserver);
        } catch (Exception e){}
        setmRecyclerView(recyclerView);
        if (notifyDatasetChanged) {
            notifyDatasetChanged();
            notifyDatasetChanged = false;
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        setmRecyclerView(null);
//        unregisterAdapterDataObserver(observer);
        adapter.unregisterAdapterDataObserver(adapterObserver);
    }

    // 这里因为不同的LayoutManager 会导致 EmptyAdapter、NetErrAdapter、LoadingAdapter 展示有问题
    protected void setmRecyclerView(RecyclerView recyclerView) {
        if (recyclerView == null) {
            this.mRecyclerView.clear();
            adapter.setLayoutManager(null);
        } else {
            //有几个adapter设置为线性布局
//            RecyclerView.LayoutManager temp = new LinearLayoutManager(recyclerView.getContext());
            if (recyclerView.getLayoutManager() != null) {//xml里设置了layoutManager
                if (this.mRecyclerView == null) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    //这里设置会影响到加载中界面
//                    if (layoutManager instanceof ILayoutManager){
//                        ((ILayoutManager) layoutManager).getDivider().divider(recyclerView);
//                    }
                    setLayoutManager(layoutManager);
                    if (layoutManager instanceof ILayoutManager){
                        ((ILayoutManager) layoutManager).getDivider().divider(recyclerView);
                    }
                }
            } else {
            }
//            recyclerView.setLayoutManager(temp);
            this.mRecyclerView = new WeakReference<>(recyclerView);
        }
    }

    /**
     * 调用刷新时，切换 adapter
     */
    protected void changeAdapterAndNotify() {
        notifyDatasetChanged();
    }

    @Deprecated
    public void notifyDatasetChanged() {
        notifyDatasetChanged = true;
        super.notifyDataSetChanged();
    }

    //=================================================
    public void setViewType(IViewType viewType) {
        adapter.setViewType(viewType);
    }

    public void setViewType(final int resource, final Class<? extends TzjViewHolder> holder) {
        adapter.setViewType(resource, holder);
    }

    /**
     * 只让设置一次
     */
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        adapter.setLayoutManager(layoutManager);
    }

    public <T> T getItem(int position) {
        return (T) adapter.getItem(position);
    }

    public List<Object> getList() {
        return adapter.getList();
    }

    public void setList(List<?> list) {
        adapter.setList(list);
    }

    public void addList(List list) {
        adapter.addList(list);
    }

    public BaseDelegate addItem(Object item) {
        adapter.addItem(item);
        return this;
    }

    public void setSelectId(int index) {
        adapter.setSelectId(index);
    }

    public int getSelectId() {
        return adapter.getSelectId();
    }

    /**
     * item 点击事件
     */
    public void setOnItemClickListener(TzjAdapter.HolderOnClick itemClickListener) {
        adapter.setOnItemClickListener(itemClickListener);
    }

    /**
     * item 长按事件
     */
    public void setOnItemLongClickListener(TzjAdapter.HolderOnClick itemLongClickListener) {
        adapter.setOnItemLongClickListener(itemLongClickListener);
    }

    /**
     * 点了具体的 view
     */
    public void setOnClickListener(TzjAdapter.HolderOnClick clickListener) {
        adapter.setOnClickListener(clickListener);
    }
}
