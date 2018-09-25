package com.tzj;

import com.tzj.recyclerview.IViewType;

/**
 * 侧滑菜单的id
 */
public interface ISwipeViewType extends IViewType {
    int getSwipeLayoutResourceId(int position);
}
