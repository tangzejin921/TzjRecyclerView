package com.tzj.view.recyclerview.holder;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.view.View;

import com.tzj.view.recyclerview.R;
import com.tzj.view.recyclerview.adapter.TzjAdapter;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;


/**
 *
 */
public class TzjViewHolder<D> extends BaseViewHolder {

    protected ViewDataBinding binding;

    protected View.OnClickListener listener;
    /**
     * view所对应的数据
     */
    protected WeakReference<D> mTarget;

    public TzjViewHolder(View itemView) {
        super(itemView);
    }


    /**
     * 创建 View 时调用，可以初始化
     * 构造方法里取不到 WLAdapter
     */
    public void onCreateView(Context ctx, TzjAdapter adapter, View itemView) {
//        binding = doBind(itemView);
    }

    /**
     * 是否需要调用刷新,
     * 如果需要判断数据是否改变
     * 请重写此方法和数据类的hashChange，调用isEques进行判断
     */
    public boolean isDoBind(final D newObj) {
        return true;
    }

    /**
     * 新数据与旧数据是否相等
     * 注意请不要调用多次，只能调用一次
     */
    protected final boolean isEques(final D newObj) {
        D oldObj = null;
        if (mTarget != null) {
            oldObj = mTarget.get();
        }
        mTarget = new WeakReference<>(newObj);
        return DiffCallBack.areContentsTheSame(oldObj,newObj,true);
    }

    /**
     *
     */
    public void onBind(TzjAdapter adapter, D item, int position) {
        if (binding != null) {
            binding.setVariable(onBrId(getClass().getSimpleName()), this);
            binding.setVariable(onBrId(item.getClass().getSimpleName()), item);
            binding.executePendingBindings();
        }
        onBind(item, position);
    }

    public void onBind(D item, int position) {

    }

    /**
     * view 的点击事件
     */
    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    /**
     * 绑定 OnclickListener
     */
    public TzjViewHolder<D> setOnClickListener(View view, int position) {
        view.setTag(R.id.item_index_tag, position);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     *
     */
    public boolean onClickable() {
        return true;
    }

    /**
     * 专为 dataBinding 调用的
     */
    public void onClick(View view) {
        int adapterPosition = getAdapterPosition();
        if (adapterPosition >= 0) {
            view.setTag(R.id.item_index_tag, adapterPosition);
            view.setOnClickListener(listener);
            //不然没有点击声音
            view.performClick();
        }
    }

    /**
     * 使用 DataBindingUtil.bind(itemView)
     */
    protected ViewDataBinding doBind(View itemView) {
        try {
            return DataBindingUtil.bind(itemView);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 请用 Holder的名称 的文件名
     * 返回 datadinding 的 BR.id
     */
    protected int onBrId(String name) {
        try {
            String packageName = itemView.getContext().getPackageName();
            Class<?> brClazz = Class.forName(packageName + ".BR");
            Field field = brClazz.getField(name);
            return field.getInt(brClazz);
        } catch (Exception e) {
            return com.tzj.view.recyclerview.BR._all;
        }
    }

    protected <T extends View> T findViewById(int id) {
        return itemView.findViewById(id);
    }

}
