package com.android.hcbd.aws.viewholder;

import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.hcbd.aws.R;
import com.android.hcbd.aws.entity.CheckDataInfo;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by guocheng on 2017/6/20.
 */

public class CheckDataViewHolder extends BaseViewHolder<CheckDataInfo> {

    private TextView tv_time;
    private TextView tv_axles_num;
    private TextView tv_limit_weight;
    private TextView tv_weight;
    private CheckBox checkBox;

    public CheckDataViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_check_data_layout);
        tv_time = $(R.id.tv_time);
        tv_axles_num = $(R.id.tv_axles_num);
        tv_limit_weight = $(R.id.tv_limit_weight);
        tv_weight = $(R.id.tv_weight);
        checkBox = $(R.id.checkBox);
    }

    @Override
    public void setData(CheckDataInfo data) {
        super.setData(data);
        tv_time.setText(data.getCreateTime());
        tv_axles_num.setText(data.getCarType().getAxisNum());
        tv_limit_weight.setText(String.valueOf(data.getCarType().getCheckLimit()));
        tv_weight.setText(String.valueOf(data.getAmt()));
        if(data.isChecked())
            checkBox.setChecked(true);
        else
            checkBox.setChecked(false);

        if(data.getOverAmt() == 0){
            tv_time.setTextColor(0xFF1A1A1A);
            tv_axles_num.setTextColor(0xFF1A1A1A);
            tv_limit_weight.setTextColor(0xFF1A1A1A);
            tv_weight.setTextColor(0xFF1A1A1A);
        }else{
            tv_time.setTextColor(0xFFFF0000);
            tv_axles_num.setTextColor(0xFFFF0000);
            tv_limit_weight.setTextColor(0xFFFF0000);
            tv_weight.setTextColor(0xFFFF0000);
        }

    }
}
