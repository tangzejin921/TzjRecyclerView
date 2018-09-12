package com.tzj.recyclerview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.TzjSpanSizeLookup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tzj.listener.NoDoubleOnClickListener;
import com.tzj.recyclerview.IViewType;
import com.tzj.recyclerview.R;
import com.tzj.recyclerview.holder.TzjViewHolder;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class TzjAdapter extends RecyclerView.Adapter<TzjViewHolder> {
    /**
     * 记录暂存用
     */
    private RecyclerView.LayoutManager layoutManager;
    /**
     * 数据
     */
    private List<IViewType> mData = new ArrayList();
    /**
     * 记录上次点击的地方
     */
    private int selectId=-1;
    /**
     * item 点击事件
     */
    private OnItemClickListener itemClickListener;
    /**
     *  item 的点事件
     */
    private View.OnClickListener itemListenerRelay = new NoDoubleOnClickListener() {
        @Override
        public void onMyClick(View v) {
            Integer i = (Integer) v.getTag(R.id.item_index_tag);
            if (i!=null && itemClickListener!=null){
                setSelectId(i);
                itemClickListener.onItemClick(TzjAdapter.this,v,i,getItem(i));
            }
        }
    };    /**
     * view 点击事件
     */
    private OnClickIndexListener clickListener;
    /**
     * view 的点击事件
     */
    private View.OnClickListener listenerRelay = new NoDoubleOnClickListener() {
        @Override
        public void onMyClick(View v) {
            Integer i = (Integer) v.getTag(R.id.item_index_tag);
            if (i!=null && clickListener!=null){
                clickListener.onClick(v,i);
            }
        }
    };
    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }
    public List<? extends IViewType> getList() {
        return mData;
    }
    public void setList(List<? extends IViewType> list) {
        this.mData  = (List<IViewType>) list;
    }
    public void addList(List<? extends IViewType> list) {
        this.mData.addAll(list);
    }
    public void addItem(IViewType item){
        this.mData.add(item);
    }
    public void setSelectId(int index){
        this.selectId = index;
    }
    public int getSelectId(){
        return selectId;
    }
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public void setClickListener(OnClickIndexListener clickListener) {
        this.clickListener = clickListener;
    }
    public <T extends IViewType> T  getItem(int position) {
        return (T) mData.get(position);
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 记录 {@link #getItemViewType(int position)} 里的 position
     * 用于 {@link #onCreateViewHolder(ViewGroup parent, int viewType)} 里获取 Class<? extends TzjViewHolder>
     */
    private int lastItemViewTypePosition;
    @Override
    public int getItemViewType(int position) {
        return getItem(lastItemViewTypePosition = position).type();
    }
    @NonNull
    @Override
    public TzjViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TzjViewHolder holder = null;
        View inflate = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        try {
            Class<? extends TzjViewHolder> holer = getItem(lastItemViewTypePosition).holder();
            Constructor<? extends TzjViewHolder> constructor = holer.getConstructor(View.class);
            holder = constructor.newInstance(inflate);
            holder.setListener(listenerRelay);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        holder.itemView.setOnClickListener(itemListenerRelay);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TzjViewHolder holder, int position) {
        holder.itemView.setTag(R.id.item_index_tag,position);
        holder.onBind(getItem(position),position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new TzjSpanSizeLookup(){
                @Override
                public int getSpanSize(int position) {
                    IViewType item = getItem(position);
                    if (item instanceof com.tzj.recyclerview.LayoutManager.GridLayoutManager.SpanSize){
                        return ((com.tzj.recyclerview.LayoutManager.GridLayoutManager.SpanSize) item).getSpanSize();
                    }else{
                        return 1;
                    }
                }
            });
        }
    }

    /**
     * itemClick
     */
    public interface OnItemClickListener{
        void onItemClick(TzjAdapter adapter,View v,int index,Object obj);
    }

    /**
     * 返回index的 click
     */
    public static abstract class OnClickIndexListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            onClick(v);
        }
        public abstract void onClick(View v,int index);
    }
}
