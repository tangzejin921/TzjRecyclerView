package com.tzj.recyclerview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tzj.recyclerview.LayoutManager.ILayoutManager;
import com.tzj.recyclerview.LayoutManager.LinearLayoutManager;


public class AdapterDelegate extends RecyclerView.Adapter {
    private RecyclerView mRecyclerView;

    //TODO 这里因为不同的LayoutManager 会导致 EmptyAdapter、NetErrAdapter、LoadingAdapter 展示有问题
    //TODO 现在这样写导致代码很乱
    private void setmRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;

        RecyclerView.LayoutManager layoutManager = emptyAdapter.getLayoutManager();
        if (layoutManager == null){
            LinearLayoutManager temp = new LinearLayoutManager(mRecyclerView.getContext());
            temp.setOrientation(RecyclerView.VERTICAL);
            layoutManager = temp;
        }
        emptyAdapter.setLayoutManager(layoutManager);
        netErrAdapter.setLayoutManager(layoutManager);
        if (loadingAdapter!=null){
            loadingAdapter.setLayoutManager(layoutManager);
        }
        //RecyclerView 设置完 layoutManager 后调用
        RecyclerView.LayoutManager temp = mRecyclerView.getLayoutManager();
        if (temp != layoutManager && temp instanceof ILayoutManager) {
            adapter.setLayoutManager(temp);
        }
        if (currentAdapter!=adapter){
            mRecyclerView.setLayoutManager(layoutManager);
        }
    }

    private EmptyAdapter emptyAdapter = new EmptyAdapter();
    private NetErrAdapter netErrAdapter = new NetErrAdapter();
    private LoadingAdapter loadingAdapter = new LoadingAdapter();
    private TzjAdapter adapter = new TzjAdapter();

    private RecyclerView.Adapter currentAdapter = loadingAdapter;

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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return currentAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        currentAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        setmRecyclerView(recyclerView);
        registerAdapterDataObserver(observer);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        unregisterAdapterDataObserver(observer);
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            notifyDataChanged();
        }
    };

    private void notifyDataChanged() {
        loadingAdapter = null;
        RecyclerView.Adapter lastAdapter = currentAdapter;//上一次的 Adapter
        if (adapter.getList().size() == 0) {
            currentAdapter = emptyAdapter;
        } else {
            currentAdapter = adapter;
        }
        if (lastAdapter != currentAdapter && lastAdapter instanceof TzjAdapter) {
            RecyclerView.LayoutManager layoutManager = ((TzjAdapter) currentAdapter).getLayoutManager();
            mRecyclerView.setLayoutManager(layoutManager);
            if (currentAdapter == adapter){
                currentAdapter.onAttachedToRecyclerView(mRecyclerView);//为了 GridLayout 的 span 设置
            }
        }
        currentAdapter.notifyDataSetChanged();
    }

    public EmptyAdapter getEmptyAdapter() {
        return emptyAdapter;
    }

    public LoadingAdapter getLoadingAdapter() {
        return loadingAdapter;
    }
}
