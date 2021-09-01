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
     * 　注意其调用次数。
     * 　此方法有可能被DiffCallBack和TzjViewHolder子类调用２次
     *  此方法与TzjViewHolder.isDoBind 应当同时重写
     * @param save 为了实现多次调用此判断，才加了此入参，
     *             一旦传入true，将保存当前hash。后续再调用判断将不准
     */
    default boolean hashChange(boolean save) {
        return true;
    }
}