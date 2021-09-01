package com.tzj.view.recyclerview.holder;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.tzj.view.recyclerview.IViewType;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认 DiffUtil.Callback
 */
public class DiffCallBack extends DiffUtil.Callback {
    private List<Object> mOldDatas = new ArrayList<>();
    private List<Object> mNewDatas = new ArrayList<>();

    public DiffCallBack(List<Object> mOldDatas, List<Object> mNewDatas) {
        this.mOldDatas = mOldDatas;
        this.mNewDatas = mNewDatas;
    }

    @Override
    public int getOldListSize() {
        return mOldDatas != null ? mOldDatas.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewDatas != null ? mNewDatas.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        //是否同类型
        Object oldObj = mOldDatas.get(oldItemPosition);
        Object newObj = mNewDatas.get(newItemPosition);
        if (oldObj instanceof IViewType && newObj instanceof IViewType) {
            return ((IViewType) oldObj).type() == ((IViewType) newObj).type();
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        //是否相同数据
        return areContentsTheSame(mOldDatas.get(oldItemPosition), mNewDatas.get(newItemPosition),false);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //这里可以判断item内部的某个变量的变化，然后到 Adapter.onBindViewHolder里进行更加细致的局部刷新
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }

    public static boolean areContentsTheSame(Object oldObj, Object newObj,boolean saveHash) {
        if (newObj == null) {
            return false;
        }
        //数据内容的比较(是否发生了变化)
        if (oldObj == newObj) {
            //同一个对象情况下，将交给hashChange来判断是否相同
            if (newObj instanceof IViewType) {
                return !((IViewType) newObj).hashChange(saveHash);
            } else {
                return false;
            }
        }
        return newObj.equals(oldObj);
    }
}
