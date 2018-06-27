package com.android.hcbd.aws.viewholder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.android.hcbd.aws.R;
import com.android.hcbd.aws.entity.PreCheckDataInfo;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import java.math.BigDecimal;

/**
 * Created by guocheng on 2017/6/20.
 */

public class PreCheckDataViewHolder extends BaseViewHolder<PreCheckDataInfo> {

    private TextView tv_time;
    private TextView tv_axles_num;
    private TextView tv_weight;
    private TextView tv_over_percent;

    public PreCheckDataViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_pre_check_data_layout);
        tv_time = $(R.id.tv_time);
        tv_axles_num = $(R.id.tv_axles_num);
        tv_weight = $(R.id.tv_weight);
        tv_over_percent = $(R.id.tv_over_percent);
    }

    @Override
    public void setData(PreCheckDataInfo data) {
        super.setData(data);
        tv_time.setText(data.getCreateTime());
        tv_axles_num.setText(data.getAxisNum());
        tv_weight.setText(String.valueOf(data.getWeight()));
        tv_over_percent.setText(data.getOverPercent());

        BigDecimal dataA = new BigDecimal(data.getOverPercent().replace("%",""));
        BigDecimal dataB = new BigDecimal("0.00");
        //大于为1，相同为0，小于为-1
        if(dataA.compareTo(dataB) == 1){
            tv_time.setTextColor(0xFFFF0000);
            tv_axles_num.setTextColor(0xFFFF0000);
            tv_weight.setTextColor(0xFFFF0000);
            tv_over_percent.setTextColor(0xFFFF0000);
        }else{
            tv_time.setTextColor(0xFF1A1A1A);
            tv_axles_num.setTextColor(0xFF1A1A1A);
            tv_weight.setTextColor(0xFF1A1A1A);
            tv_over_percent.setTextColor(0xFF1A1A1A);
        }
    }
}
