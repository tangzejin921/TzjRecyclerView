package com.tzj.recyclerview.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.daimajia.swipe.interfaces.SwipeAdapterInterface;
import com.tzj.recyclerview.LayoutManager.ILayoutManager;
import com.tzj.recyclerview.LayoutManager.LinearLayoutManager;
import com.tzj.recyclerview.TzjRecyclerView;

public class AdapterDelegate extends RecyclerView.Adapter implements SwipeAdapterInterface {
    private RecyclerView mRecyclerView;

    private EmptyAdapter emptyAdapter = new EmptyAdapter();
    private NetErrAdapter netErrAdapter = new NetErrAdapter();
    private LoadingAdapter loadingAdapter = new LoadingAdapter();
    private TzjAdapter adapter = new TzjAdapter();

    private RecyclerView.Adapter currentAdapter = loadingAdapter;

    /**
     * notifyDataChanged
     */
    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            notifyDataChanged();
        }
    };
    /**
     * 网络监听
     */
    private NetBroadcastReceiver receiver = new NetBroadcastReceiver();

    public AdapterDelegate() {
        //todo 这里会导致内存泄漏吗？
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                notifyDataSetChanged();
            }
        });
    }

    public TzjAdapter getAdapter() {
        return adapter;
    }

    @Override
    public int getItemCount() {
        return currentAdapter.getItemCount();
    }

    private int tempLastId;
    @Override
    public int getItemViewType(int position) {
        return currentAdapter.getItemViewType(tempLastId = position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = currentAdapter.onCreateViewHolder(parent, viewType);
        if (mRecyclerView instanceof TzjRecyclerView && ((TzjRecyclerView)mRecyclerView).getItemManger()!=null){
            ((TzjRecyclerView)mRecyclerView).getItemManger().onCreate(viewHolder.itemView,tempLastId);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mRecyclerView instanceof TzjRecyclerView && ((TzjRecyclerView)mRecyclerView).getItemManger()!=null){
            ((TzjRecyclerView)mRecyclerView).getItemManger().bind(holder.itemView,position);
        }
        currentAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        setmRecyclerView(recyclerView);//setAdapter 时会调用
        registerAdapterDataObserver(observer);
        if (recyclerView.getContext()!=null){
            receiver.setConnect(recyclerView.getContext());
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            recyclerView.getContext().registerReceiver(receiver,filter);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        unregisterAdapterDataObserver(observer);
        if (recyclerView.getContext()!=null){
            recyclerView.getContext().unregisterReceiver(receiver);
        }
    }
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
        if (currentAdapter!=adapter){//setAdapter 时设置为其他的布局
            mRecyclerView.setLayoutManager(layoutManager);
        }
    }


    private void notifyDataChanged() {
        loadingAdapter = null;
        RecyclerView.Adapter lastAdapter = currentAdapter;//上一次的 Adapter
        if (receiver.isConnect()){
            if (adapter.getList().size() == 0) {
                currentAdapter = emptyAdapter;
            } else {
                currentAdapter = adapter;
            }
        }else{
            currentAdapter = netErrAdapter;
        }
        if (lastAdapter != currentAdapter && lastAdapter instanceof TzjAdapter) {
            RecyclerView.LayoutManager layoutManager = ((TzjAdapter) currentAdapter).getLayoutManager();
            mRecyclerView.setLayoutManager(layoutManager);
            if (currentAdapter == adapter){
                currentAdapter.onAttachedToRecyclerView(mRecyclerView);//为了 GridLayout 的 span 设置
            }
        }
    }

    public EmptyAdapter getEmptyAdapter() {
        return emptyAdapter;
    }

    public LoadingAdapter getLoadingAdapter() {
        return loadingAdapter;
    }

    class NetBroadcastReceiver extends BroadcastReceiver {
        private boolean isConnect;

        public boolean isConnect() {
            return isConnect;
        }
        public void setConnect(Context connect) {
            isConnect = connect(connect);
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                boolean temp = connect(context);
                if (temp != isConnect){
                    isConnect = temp;
                    notifyDataChanged();
                }
            }
        }
        private boolean connect(Context ctx){
            ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) { // 连接上网络
                return true;
            } else {   // 没有连接上
                return false;
            }
        }
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        if (currentAdapter instanceof TzjAdapter){
            return ((TzjAdapter) currentAdapter).getSwipeLayoutResourceId(position);
        }
        return 0;
    }

    @Override
    public void notifyDatasetChanged() {
        super.notifyDataSetChanged();
    }
}

