package com.tzj.recyclerview.holder;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.daimajia.swipe.SwipeLayout;
import com.tzj.recyclerview.BR;
import com.tzj.recyclerview.R;
import com.tzj.recyclerview.adapter.TzjAdapter;

import java.lang.reflect.Field;

public class TzjViewHolder<D> extends RecyclerView.ViewHolder {

    protected ViewDataBinding binding;

    protected View.OnClickListener listener;
    protected SwipeLayout swipeLayout;
    /**
     * 是否 binded 了
     */
    public boolean isFirstBinded = false;

    /**
     * view 的点击事件
     */
    public void setListener(View.OnClickListener listener) {
        if (swipeLayout != null){
            final View.OnClickListener temp = listener;
            //这个为了关闭 侧滑
            this.listener = new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    temp.onClick(v);
                    //关闭侧滑
                    swipeLayout.close();
                }
            };
        }else{
            this.listener = listener;
        }
    }

    /**
     * 让 TzjAdapter 调用
     */
    public void setSwipeLayout(SwipeLayout swipeLayout) {
        this.swipeLayout = swipeLayout;
    }

    public SwipeLayout getSwipeLayout() {
        return swipeLayout;
    }

    public TzjViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * 创建 View 时调用，可以初始化
     * 构造方法里取不到 TzjAdapter
     */
    public void onCreateView(Context ctx, TzjAdapter adapter, View itemView) {
        try {
            binding = DataBindingUtil.bind(itemView);
        }catch (Exception e){}
    }

    /**
     *
     */
    public void onBind(TzjAdapter adapter, D d, int position) {
        if (binding != null) {
            binding.setVariable(onBrId(getClass().getSimpleName()), this);
            binding.setVariable(onBrId(d.getClass().getSimpleName()), d);
            binding.executePendingBindings();
        }
    }

    /**
     * findViewById
     */
    public <T extends View> T bind(int r) {
        return itemView.findViewById(r);
    }

    /**
     * 绑定 OnclickListener
     */
    public void setOnClickListener(View view, int position) {
        view.setTag(R.id.item_index_tag, position);
        view.setOnClickListener(listener);
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
        if (adapterPosition >= 0 ){
            view.setTag(R.id.item_index_tag, adapterPosition);
            view.setOnClickListener(listener);
            //不然没有点击声音
            view.performClick();
        }
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
            return BR._all;
        }
    }
}
