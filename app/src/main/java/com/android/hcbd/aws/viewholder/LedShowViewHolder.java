package com.android.hcbd.aws.viewholder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.android.hcbd.aws.R;
import com.android.hcbd.aws.entity.LedShowInfo;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by 14525 on 2017/7/11.
 */

public class LedShowViewHolder extends BaseViewHolder<LedShowInfo> {

    private TextView tv_time;
    //private TextView tv_name;
    private TextView tv_value;

    public LedShowViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_led_show_layout);
        tv_time = $(R.id.tv_time);
        //tv_name = $(R.id.tv_name);
        tv_value = $(R.id.tv_value);
    }

    @Override
    public void setData(LedShowInfo data) {
        super.setData(data);
        tv_time.setText(data.getCreateTime().replace("T",/*"\n"*/""));
        //tv_name.setText(data.getName());
        tv_value.setText(data.getRemark());
    }
}
