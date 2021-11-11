package com.tzj.view.recyclerview;

import com.tzj.view.recyclerview.holder.TzjViewHolder;

public interface IViewType {
    /**
     * 这里直接给 布局
     * 注意：一个布局对应的类不能重复。
     */
    int type();

    /**
     * 这里提供布局对应的 Holder
     */
    default Class<? extends TzjViewHolder> holder() {
        return TzjViewHolder.class;
    }

    /**
     * 　用于判断同一个对象是否改变，
     * 　注意其调用次数。当onBind之后将会重新记录hashCode
     * 　此方法有可能被DiffCallBack和TzjViewHolder子类调用
     *  此方法与TzjViewHolder.isDoBind 应当同时重写
     * @param save 过时了，不需要此参数了
     */
    default boolean hashChange(boolean save) {
        return true;
    }

    /**
     * adapter 调用刷新(onBind)之前会调用此方法
     * 可以在此方法里记录 hashCode，
     * 然后hashChange里用记录的hashCode与自身hashCode对比
     */
    default void onRefresh(){}
}