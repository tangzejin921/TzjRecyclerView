package com.tzj.view.recyclerview.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tzj.view.listener.NoDoubleOnClickListener;
import com.tzj.view.recyclerview.DefaultViewType;
import com.tzj.view.recyclerview.IViewType;
import com.tzj.view.recyclerview.holder.WLViewHolder;
import com.tzj.view.recyclerview.layoutmanager.LinearLayoutManager;

import java.lang.ref.WeakReference;
import java.util.List;

public class AdapterDelegate extends RecyclerView.Adapter {
    public interface ICreateViewType {
        IViewType createEmpty();

        IViewType createNetErr();

        IViewType createLoading();
    }

    public static ICreateViewType iCreateAdapter = new ICreateViewType() {
        @Override
        public IViewType createEmpty() {
            return new DefaultViewType();
        }

        @Override
        public IViewType createNetErr() {
            return new DefaultViewType();
        }

        @Override
        public IViewType createLoading() {
            return new DefaultViewType();
        }
    };
    private WeakReference<RecyclerView> mRecyclerView;
    /**
     * 空类容的 adapter
     */
    private WLAdapter emptyAdapter = new WLAdapter().addItem(iCreateAdapter.createEmpty());
    public void setEmptyAdapter(WLAdapter emptyAdapter) {
        this.emptyAdapter = emptyAdapter;
    }
    /**
     * 网络异常的 adapter
     */
    private WLAdapter netErrAdapter = new WLAdapter().addItem(iCreateAdapter.createNetErr());
    public void setNetErrAdapter(WLAdapter netErrAdapter) {
        this.netErrAdapter = netErrAdapter;
    }
    /**
     * 加载中的 adapter
     */
    private WLAdapter loadingAdapter = new WLAdapter().addItem(iCreateAdapter.createLoading());
    public void setLoadingAdapter(WLAdapter loadingAdapter) {
        this.loadingAdapter = loadingAdapter;
        currentAdapter = loadingAdapter;
    }
    /**
     * 真实数据的 adapter
     */
    private WLAdapter adapter = new WLAdapter();
    /**
     * 当前的 Adapter
     */
    private RecyclerView.Adapter currentAdapter = loadingAdapter;

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
            notifyDataSetChanged();
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

    /**
     * 网络监听
     */
    private NetBroadcastReceiver receiver = new NetBroadcastReceiver(this);

    public AdapterDelegate() {
        /**
         * 这里设置 true，然后 设置 getItemId，
         * 调用 notifyDataChanged 界面上的view 不会刷新
         * 后来发现并不管用，反而会导致拖动交换不停刷新
         */
//        setHasStableIds(true);
//        adapter.setHasStableIds(true);
        adapter.registerAdapterDataObserver(adapterObserver);
    }

    public WLAdapter getAdapter() {
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
        if (recyclerView.getContext() != null) {
            receiver.registerReceiver(recyclerView.getContext());
        }
        registerAdapterDataObserver(observer);
//        adapter.registerAdapterDataObserver(adapterObserver);
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
        if (recyclerView.getContext() != null) {
            receiver.unRegisterReceiver(recyclerView.getContext());
        }
        unregisterAdapterDataObserver(observer);
        adapter.unregisterAdapterDataObserver(adapterObserver);
    }

    // 这里因为不同的LayoutManager 会导致 EmptyAdapter、NetErrAdapter、LoadingAdapter 展示有问题
    private void setmRecyclerView(RecyclerView recyclerView) {
        if (recyclerView == null) {
            this.mRecyclerView.clear();
            emptyAdapter.setLayoutManager(null);
            netErrAdapter.setLayoutManager(null);
            loadingAdapter.setLayoutManager(null);
            adapter.setLayoutManager(null);
        } else {
            //有几个adapter设置为线性布局
            RecyclerView.LayoutManager temp = new LinearLayoutManager(recyclerView.getContext());
            emptyAdapter.setLayoutManager(temp);
            netErrAdapter.setLayoutManager(temp);
            loadingAdapter.setLayoutManager(temp);
            if (recyclerView.getLayoutManager() != null && this.mRecyclerView == null) {//xml里设置了layoutManager
                adapter.setLayoutManager(recyclerView.getLayoutManager());
            }
            recyclerView.setLayoutManager(temp);
            this.mRecyclerView = new WeakReference<>(recyclerView);
        }
    }

    /**
     * 调用刷新时，切换 adapter
     */
    private void changeAdapterAndNotify() {
        synchronized (currentAdapter) {
            loadingAdapter = null;
            RecyclerView.Adapter lastAdapter = currentAdapter;//上一次的 Adapter
            if (receiver.isConnect()) {
                if (adapter.getList().size() == 0) {
                    currentAdapter = emptyAdapter;
                } else {
                    currentAdapter = adapter;
                }
            } else {
                currentAdapter = netErrAdapter;
            }
            if (lastAdapter != currentAdapter && lastAdapter instanceof WLAdapter) {
                RecyclerView.LayoutManager layoutManager = ((WLAdapter) currentAdapter).getLayoutManager();
                if (mRecyclerView != null) {
                    RecyclerView recyclerView = mRecyclerView.get();
                    if (recyclerView != null) {
                        if (layoutManager != null) {
                            recyclerView.setLayoutManager(layoutManager);
                        }
                        if (currentAdapter == adapter) {
                            currentAdapter.onAttachedToRecyclerView(recyclerView);//为了 GridLayout 的 span 设置
                        }
                    }
                }
            }
        }
    }


    public static class NetBroadcastReceiver extends BroadcastReceiver {
        private boolean isConnect;
        private static final String checkNetBroadcast = "com.tzj.recyclerview.adapter.CHECK_NETWORK";

        public static void sendBroadcast(Context ctx) {
            Intent intent = new Intent(checkNetBroadcast);
            ctx.sendBroadcast(intent);
        }

        private AdapterDelegate adapter;

        public NetBroadcastReceiver(AdapterDelegate adapter) {
            this.adapter = adapter;
        }

        public boolean isConnect() {
            return isConnect;
        }

        public void setConnect(Context connect) {
            isConnect = connect(connect);
        }

        public void registerReceiver(Context ctx) {
            setConnect(ctx);
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(checkNetBroadcast);
            ctx.registerReceiver(this, filter);
        }

        public void unRegisterReceiver(Context ctx) {
            ctx.unregisterReceiver(this);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)
                    || intent.getAction().equals(Intent.ACTION_SCREEN_ON)
                    || intent.getAction().equals(checkNetBroadcast)) {
                boolean temp = connect(context);
                if (temp != isConnect) {
                    isConnect = temp;
                    adapter.notifyDatasetChanged();
                }
            }
        }

        private boolean connect(Context ctx) {
            ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) { // 连接上网络
                return true;
            } else {   // 没有连接上
                return false;
            }
        }
    }

    public void notifyDatasetChanged() {
        notifyDatasetChanged = true;
        super.notifyDataSetChanged();
    }

    //=================================================
    public void setViewType(IViewType viewType) {
        adapter.setViewType(viewType);
    }

    public void setViewType(final int resource, final Class<? extends WLViewHolder> holder) {
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

    public List<?> getList() {
        return adapter.getList();
    }

    public void setList(List<?> list) {
        adapter.setList(list);
    }

    public void addList(List list) {
        adapter.addList(list);
    }

    public AdapterDelegate addItem(Object item) {
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
    public void setOnItemClickListener(NoDoubleOnClickListener itemClickListener) {
        adapter.setOnItemClickListener(itemClickListener);
    }

    /**
     * item 长按事件
     */
    public void setOnItemLongClickListener(NoDoubleOnClickListener itemLongClickListener) {
        adapter.setOnItemLongClickListener(itemLongClickListener);
    }

    /**
     * 点了具体的 view
     */
    public void setOnClickListener(NoDoubleOnClickListener clickListener) {
        adapter.setOnClickListener(clickListener);
    }
}
