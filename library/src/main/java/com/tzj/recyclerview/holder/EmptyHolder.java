package com.tzj.recyclerview.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tzj.recyclerview.R;
import com.tzj.recyclerview.adapter.TzjAdapter;
import com.tzj.recyclerview.entity.Empty;

/**
 * 空数据
 */
public class EmptyHolder extends TzjViewHolder<Empty>{
    private ImageView imageView;
    private TextView text;
    private TextView hint;

    public EmptyHolder(View itemView) {
        super(itemView);
        imageView =bind(R.id.empty_iv);
        text =bind(R.id.empty_tv);
        hint =bind(R.id.empty_hint);
    }

    @Override
    public void onBind(TzjAdapter adapter,Empty empty, int position) {
        super.onBind(adapter,empty, position);
        imageView.setImageResource(empty.getImageRes());
        text.setText(empty.getText());
        hint.setText(empty.getHint());
    }
}
