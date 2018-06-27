package com.android.hcbd.aws.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.hcbd.aws.R;
import com.android.hcbd.aws.entity.PreHistoryInfo;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import java.math.BigDecimal;

/**
 * Created by guocheng on 2017/6/20.
 */

public class PreCheckHistoryViewHolder extends BaseViewHolder<PreHistoryInfo> {

    private TextView tv_time;
    private TextView tv_axles_num;
    private TextView tv_weight;
    private TextView tv_overPercent;
    private ImageView iv;
    private LinearLayout ll;
    private TextView tv_code;
    private TextView tv_limit_weight;
    private TextView tv_minAmt;
    private TextView tv_carNO;
    private TextView tv_lane;
    private TextView tv_speed;

    public PreCheckHistoryViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_precheck_data_history_layout);
        tv_time = $(R.id.tv_time);
        tv_axles_num = $(R.id.tv_axles_num);
        tv_weight = $(R.id.tv_weight);
        tv_overPercent = $(R.id.tv_overPercent);
        iv = $(R.id.iv);
        ll = $(R.id.ll);
        tv_code = $(R.id.tv_code);
        tv_limit_weight = $(R.id.tv_limit_weight);
        tv_minAmt = $(R.id.tv_minAmt);
        tv_carNO = $(R.id.tv_carNO);
        tv_lane = $(R.id.tv_lane);
        tv_speed = $(R.id.tv_speed);
    }

    @Override
    public void setData(PreHistoryInfo data) {
        super.setData(data);
        tv_time.setText(""+data.getCreateTime());
        tv_axles_num.setText(""+data.getAxisNum());
        tv_weight.setText(""+String.valueOf(data.getWeight()));
        tv_overPercent.setText(""+data.getOverPercent());

        tv_code.setText("流水号："+data.getCode());
        tv_limit_weight.setText("限重："+data.getLimitAmt());
        tv_minAmt.setText("超限吨数："+data.getMinAmt());
        tv_carNO.setText("车牌："+data.getCarNo());
        tv_lane.setText("车道："+data.getLane());
        tv_speed.setText("速度："+data.getSpeed());

        iv.setSelected(false);
        ll.setVisibility(View.GONE);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(iv.isSelected()){
                    iv.setSelected(false);
                    ll.setVisibility(View.GONE);
                }
                else{
                    iv.setSelected(true);
                    ll.setVisibility(View.VISIBLE);
                }
            }
        });

        BigDecimal dataA = new BigDecimal(data.getOverPercent().replace("%",""));
        BigDecimal dataB = new BigDecimal("0.00");
        if(dataA.compareTo(dataB) == 1){
            tv_time.setTextColor(0xFFFF0000);
            tv_axles_num.setTextColor(0xFFFF0000);
            tv_weight.setTextColor(0xFFFF0000);
            tv_overPercent.setTextColor(0xFFFF0000);
        }else{
            tv_time.setTextColor(0xFF1A1A1A);
            tv_axles_num.setTextColor(0xFF1A1A1A);
            tv_weight.setTextColor(0xFF1A1A1A);
            tv_overPercent.setTextColor(0xFF1A1A1A);
        }


    }
}
