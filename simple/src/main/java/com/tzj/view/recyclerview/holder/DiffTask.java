package com.tzj.view.recyclerview.holder;

import android.os.AsyncTask;
import android.support.v7.util.DiffUtil;

import com.tzj.view.recyclerview.adapter.TzjAdapter;

import java.lang.ref.WeakReference;
import java.util.List;

public class DiffTask extends AsyncTask<List<Object>, Void, DiffUtil.DiffResult> {
    private final WeakReference<TzjAdapter> mAdapter;
    private WeakReference<List<Object>> newDate;

    public DiffTask(TzjAdapter adapter) {
        mAdapter = new WeakReference<>(adapter);
    }

    @Override
    protected DiffUtil.DiffResult doInBackground(List<Object>... lists) {
        TzjAdapter tzjAdapter = mAdapter.get();
        if (tzjAdapter == null) {
            return null;
        }
        newDate = new WeakReference<>(lists[0]);
//        //如果List没改变，返回null使用　notifyDataSetChanged
//        if (tzjAdapter.getList() == lists[0]){
//            return null;
//        } else {
        return DiffUtil.calculateDiff(new DiffCallBack(tzjAdapter.getList(), lists[0]));
//        }
    }

    @Override
    protected void onPostExecute(DiffUtil.DiffResult diffResult) {
        super.onPostExecute(diffResult);
        TzjAdapter adapter = mAdapter.get();
        if (adapter != null) {
            if (diffResult == null) {
                adapter.notifyDataSetChanged();
            } else {
                diffResult.dispatchUpdatesTo(adapter);
            }
            List<Object> list = newDate.get();
            if (list != null) {
                adapter.setList(list);
            }
        }
    }
}
