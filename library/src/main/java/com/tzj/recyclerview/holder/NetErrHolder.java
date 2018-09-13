package com.tzj.recyclerview.holder;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.tzj.recyclerview.R;
import com.tzj.recyclerview.entity.NetErr;

public class NetErrHolder extends TzjViewHolder<NetErr>{
    private Button button;
    public NetErrHolder(View itemView) {
        super(itemView);
        button = bind(R.id.net_err_bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                v.getContext().startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); //直接进入手机中的wifi网络设置界面
                v.getContext().startActivity(new Intent(Settings.ACTION_SETTINGS)); //直接进入手机中设置界面
            }
        });
    }
}
