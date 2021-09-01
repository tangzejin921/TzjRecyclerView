package com.tzj.view.recyclerview.adapter;

import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;

import com.tzj.view.recyclerview.R;
import com.tzj.view.recyclerview.holder.TzjViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 这里实现了BaseAdapter用于ListView.
 * RecyclerView 刷新时常常会创建多个Holder
 *
 * 适合使用RecyclerView场景：
 * 1.页面只有一页情况。
 * 2.或者数据类型只有种
 */
public abstract class TzjLVAdapter
        extends RecyclerView.Adapter<TzjViewHolder>
        implements ListAdapter, SpinnerAdapter {
    //    private final DataSetObservable mDataSetObservable = new DataSetObservable();
    private CharSequence[] mAutofillOptions = null;
    private HashMap<DataSetObserver, ProxObserver> mMap = new HashMap<>();
//    public boolean hasStableIds() {
//        return false;
//    }

    public void registerDataSetObserver(DataSetObserver observer) {
//        mDataSetObservable.registerObserver(observer);
        mMap.put(observer, new ProxObserver(observer));
        registerAdapterDataObserver(mMap.get(observer));
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
//        mDataSetObservable.unregisterObserver(observer);
        ProxObserver proxObserver = mMap.get(observer);
        if (proxObserver != null) {
            unregisterAdapterDataObserver(proxObserver);
        }
    }

//    /**
//     * Notifies the attached observers that the underlying data has been changed
//     * and any View reflecting the data set should refresh itself.
//     */
//    public void notifyDataSetChanged() {
//        mDataSetObservable.notifyChanged();
//    }

    /**
     * Notifies the attached observers that the underlying data is no longer valid
     * or available. Once invoked this adapter is no longer valid and should
     * not report further data set changes.
     */
    public void notifyDataSetInvalidated() {
//        mDataSetObservable.notifyInvalidated();
    }

    public boolean areAllItemsEnabled() {
        return true;
    }

    public boolean isEnabled(int position) {
        return true;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        return mAutofillOptions;
    }

    /**
     * Sets the value returned by {@link #getAutofillOptions()}
     */
    public void setAutofillOptions(CharSequence... options) {
        mAutofillOptions = options;
    }

    private static class ProxObserver extends RecyclerView.AdapterDataObserver {
        private WeakReference<DataSetObserver> mProx;

        public ProxObserver(DataSetObserver observer) {
            mProx = new WeakReference<>(observer);
        }

        @Override
        public void onChanged() {
            super.onChanged();
            DataSetObserver dataSetObserver = mProx.get();
            if (dataSetObserver != null) {
                dataSetObserver.onChanged();
            }
        }


        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            onChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            onChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            onChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            onChanged();
        }
    }

    //BaseAdapter ==================================================
    @Override
    public int getCount() {
        return getItemCount();
    }

    public Object getItem(int position) {
        return mData.get(position % mData.size());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TzjViewHolder holder = null;
        int needViewType = getItemViewType(position);
        if (convertView != null) {
            holder = (TzjViewHolder) convertView.getTag(R.id.listView_holder_tag);
            //不为null,判断类型
            if (holder != null && needViewType != holder.getItemViewType()) {
                holder = null;
            }
        }
        if (holder == null) {
            holder = createViewHolder(parent, needViewType);
            convertView = holder.itemView;
            convertView.setTag(R.id.listView_holder_tag, holder);
        }

        bindViewHolder(holder,position);
        return convertView;
    }

    //==========================================
    protected List<Object> mData = new ArrayList<Object>();
}
