package com.android.hcbd.aws.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.hcbd.aws.R;
import com.android.hcbd.aws.entity.CheckDataInfo;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by guocheng on 2017/6/20.
 */

public class CheckDataHistoryViewHolder extends BaseViewHolder<CheckDataInfo> {

    private TextView tv_time;
    private TextView tv_axles_num;
    private TextView tv_limit_weight;
    private TextView tv_weight;
    private ImageView iv;
    private LinearLayout ll;
    private TextView tv_code;
    private TextView tv_carNo;
    private TextView tv_lane;
    private TextView tv_goods;
    private TextView tv_checkTime;
    private TextView tv_checkAmt;
    private TextView tv_preCheckTime;
    private TextView tv_preCheckAmt;

    public CheckDataHistoryViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_check_data_history_layout);
        tv_time = $(R.id.tv_time);
        tv_axles_num = $(R.id.tv_axles_num);
        tv_limit_weight = $(R.id.tv_limit_weight);
        tv_weight = $(R.id.tv_weight);
        iv = $(R.id.iv);
        ll = $(R.id.ll);
        tv_code = $(R.id.tv_code);
        tv_carNo = $(R.id.tv_carNo);
        tv_lane = $(R.id.tv_lane);
        tv_goods = $(R.id.tv_goods);
        tv_checkTime = $(R.id.tv_checkTime);
        tv_checkAmt = $(R.id.tv_checkAmt);
        tv_preCheckTime = $(R.id.tv_preCheckTime);
        tv_preCheckAmt = $(R.id.tv_preCheckAmt);
    }

    @Override
    public void setData(CheckDataInfo data) {
        super.setData(data);
        tv_time.setText(data.getCreateTime());
        tv_axles_num.setText(data.getCarType().getAxisNum());
        tv_limit_weight.setText(String.valueOf(data.getCarType().getCheckLimit()));
        tv_weight.setText(String.valueOf(data.getAmt()));
        tv_code.setText("流水号："+data.getCode());
        tv_carNo.setText("车牌："+data.getCarNo());
        tv_lane.setText("初检车道："+data.getLane());
        tv_goods.setText("货物："+data.getGoods());
        tv_checkTime.setText("复检时间："+ (String.valueOf(data.getCheckTime()).equals("null") ? "":data.getCheckTime()));
        tv_checkAmt.setText("复检总重："+(String.valueOf(data.getCheckAmt()).equals("null") ? "":data.getCheckAmt()));
        if(String.valueOf(data.getPreCheckData()).equals("null")){
            tv_preCheckTime.setText("预检时间：");
            tv_preCheckAmt.setText("预检总重：");
        }else{
            Gson gson = new Gson();
            try {
                JSONObject jsonObject = new JSONObject(gson.toJson(data.getPreCheckData()));
                tv_preCheckTime.setText("预检时间："+jsonObject.getString("createTime"));
                tv_preCheckAmt.setText("预检总重："+jsonObject.getString("weight"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


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
