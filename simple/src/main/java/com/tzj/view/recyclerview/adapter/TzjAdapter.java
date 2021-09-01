package com.tzj.view.recyclerview.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.TzjSpanSizeLookup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.tzj.view.listener.NoDoubleOnClickListener;
import com.tzj.view.recyclerview.IViewType;
import com.tzj.view.recyclerview.R;
import com.tzj.view.recyclerview.holder.TzjViewHolder;

import java.lang.reflect.Constructor;
import java.util.List;


public class TzjAdapter extends TzjLVAdapter {
    /**
     * 记录暂存用
     */
    private RecyclerView.LayoutManager layoutManager;

    protected IViewType viewType;
    /**
     * view 的点击事件用 Tag 取index吗？
     */
    public boolean tagIndex = true;
    /**
     * 记录上次点击的地方
     */
    private int selectId = -1;
    /**
     * 设置maxCount用于循环显示
     */
    private boolean mMaxCount = false;

    public TzjAdapter() {
    }

    public TzjAdapter(List data) {
        mData = data;
    }

    /**
     * 记录 {@link #getItemViewType(int position)} 里的 position
     * 用于 {@link #onCreateViewHolder(ViewGroup parent, int viewType)} 里获取 Class<? extends ViewHolder>
     */
    private int lastItemViewTypePosition;

    public void setViewType(IViewType viewType) {
        this.viewType = viewType;
    }

    public void setViewType(final int resource, final Class<? extends TzjViewHolder> holder) {
        this.viewType = new IViewType() {
            @Override
            public int type() {
                return resource;
            }

            @Override
            public Class<? extends TzjViewHolder> holder() {
                return holder;
            }
        };
    }
    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    /**
     * 只让设置一次,
     * 如果想要改变请先设置null再设置
     */
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (this.layoutManager == null || layoutManager == null){
            this.layoutManager = layoutManager;
        }
    }

    public List<Object> getList() {
        return mData;
    }

    public void setList(List<?> list) {
        this.mData = (List<Object>) list;
    }

    public void addList(List list) {
        this.mData.addAll(list);
    }

    public TzjAdapter addItem(Object item) {
        this.mData.add(item);
        return this;
    }

    public void setSelectId(int index) {
        this.selectId = index;
    }

    public int getSelectId() {
        return selectId;
    }

    public void setMaxCount(boolean maxCount) {
        this.mMaxCount = maxCount;
    }

    public boolean isMaxCount() {
        return mMaxCount;
    }

    @Override
    public int getItemCount() {
        if (mMaxCount){
            return Integer.MAX_VALUE;
        }
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getViewId(lastItemViewTypePosition = position);
    }

    public int getViewId(int index) {
        if (viewType != null) {
            return viewType.type();
        } else {
            Object item = getItem(index);
            if (item instanceof IViewType) {
                return ((IViewType) item).type();
            } else {
                throw new RuntimeException("请设置 viewType 或者 集合类实现IViewType");
            }
        }
    }

    public Class<? extends TzjViewHolder> getHolder(int index) {
        if (viewType != null) {
            return viewType.holder();
        } else {
            Object item = getItem(index);
            if (item instanceof IViewType) {
                return ((IViewType) item).holder();
            } else {
                throw new RuntimeException("请设置 viewType 或者 集合类实现IViewType");
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public TzjViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TzjViewHolder holder = null;
        View inflate = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        Class<? extends TzjViewHolder> holer = getHolder(lastItemViewTypePosition);
        try {
            Constructor<? extends TzjViewHolder> constructor = holer.getConstructor(View.class);
            holder = constructor.newInstance(inflate);
            holder.onCreateView(parent.getContext(), this, holder.itemView);
            holder.setListener(new HolderOnClick(holder) {
                @Override
                public void onMyClick(View v, int index) {
                    if (clickListener != null) {
                        if (!tagIndex){
                            index = holder.getAdapterPosition();
                        }
                        clickListener.onMyClick(v, index);
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (holder.onClickable()) {
            holder.itemView.setOnClickListener(new HolderOnClick(holder) {
                @Override
                public void onMyClick(View v, int index) {
                    setSelectId(index);
                    if (itemClickListener != null) {
                        itemClickListener.onMyClick(v, index);
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new HolderOnClick(holder) {
                @Override
                public void onMyClick(View v, int index) {
                    setSelectId(index);
                    if (itemLongClickListener != null) {
                        itemLongClickListener.onMyClick(v, index);
                    }
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TzjViewHolder holder, int position) {
        Object newObj = getItem(position);
        if (holder.isDoBind(newObj)) {
            holder.itemView.setTag(R.id.item_index_tag, position);
            holder.onBind(this, newObj, position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new TzjSpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    Object obj= getItem(position);
                    if (obj instanceof IViewType){
                        IViewType item = (IViewType) obj;
                        if (item instanceof com.tzj.view.recyclerview.layoutmanager.GridLayoutManager.SpanSize) {
                            int temp = ((com.tzj.view.recyclerview.layoutmanager.GridLayoutManager.SpanSize) item).getSpanSize();
                            if (temp < 1){
                                temp = gridManager.getSpanCount();
                            }
                            return temp;
                        }
                    }
                    return 1;
                }
            });
        }
    }

    /**
     * item 点击事件
     */
    private NoDoubleOnClickListener itemClickListener;
    /**
     * item 长按
     */
    private NoDoubleOnClickListener itemLongClickListener;

    /**
     * view 的点击事件
     */
    private NoDoubleOnClickListener clickListener;

    /**
     * item 点击事件
     */
    public void setOnItemClickListener(TzjAdapter.HolderOnClick itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * item 长按事件
     */
    public void setOnItemLongClickListener(TzjAdapter.HolderOnClick itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    /**
     * 点了具体的 view
     */
    public void setOnClickListener(TzjAdapter.HolderOnClick clickListener) {
        this.clickListener = clickListener;
    }


    public abstract static class HolderOnClick extends NoDoubleOnClickListener {
        protected TzjViewHolder holder;

        public HolderOnClick() {
        }

        public HolderOnClick(TzjViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public abstract void onMyClick(View view, int index);

        @Override
        public void onMyClick(View view) {
            throw new RuntimeException("无效方法");
        }
    }

}
