package com.android.hcbd.aws.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.hcbd.aws.R;
import com.android.hcbd.aws.entity.CarTypeInfo;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by guocheng on 2017/7/25.
 */

public class CarTypeViewHolder extends BaseViewHolder<CarTypeInfo> {

    private TextView tv_name;
    private TextView tv_axles_num;
    private TextView tv_preCheckAmt;
    private TextView tv_checkAmt;
    private ImageView iv;
    private LinearLayout ll;
    private TextView tv_01;
    private TextView tv_02;

    public CarTypeViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_car_type_layout);
        tv_name = $(R.id.tv_name);
        tv_axles_num = $(R.id.tv_axles_num);
        tv_preCheckAmt = $(R.id.tv_preCheckAmt);
        tv_checkAmt = $(R.id.tv_checkAmt);
        iv = $(R.id.iv);
        ll = $(R.id.ll);
        tv_01 = $(R.id.tv_01);
        tv_02 = $(R.id.tv_02);
    }

    @Override
    public void setData(CarTypeInfo data) {
        super.setData(data);
        tv_name.setText(data.getName());
        tv_axles_num.setText(data.getAxisNum());
        tv_preCheckAmt.setText(""+data.getLimit());
        tv_checkAmt.setText(""+data.getCheckLimit());
        tv_01.setText("是否是超限参考值："+data.getIsDefName());
        tv_02.setText("LED是否显示："+data.getIsShowName());
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
    }
}
