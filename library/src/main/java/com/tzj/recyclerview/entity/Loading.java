package com.tzj.recyclerview.entity;

import com.tzj.recyclerview.IViewType;
import com.tzj.recyclerview.R;
import com.tzj.recyclerview.holder.TzjViewHolder;

/**
 * Copyright © 2019 健康无忧网络科技有限公司<br>
 * Author:      唐泽金 tangzejin921@qq.com<br>
 * Version:     1.0.0<br>
 * Date:        2019-04-26 14:42<br>
 * Description: 加载中
 */
public class Loading implements IViewType {
    @Override
    public int type() {
        return R.layout.view_loading;
    }
    @Override
    public Class<? extends TzjViewHolder> holder() {
        return TzjViewHolder.class;
    }
}
