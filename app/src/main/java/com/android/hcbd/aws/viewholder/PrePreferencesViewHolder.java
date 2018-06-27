package com.android.hcbd.aws.viewholder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.android.hcbd.aws.R;
import com.android.hcbd.aws.entity.PrePreferencesInfo;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by 14525 on 2017/6/26.
 */

public class PrePreferencesViewHolder extends BaseViewHolder<PrePreferencesInfo> {

    private TextView tv_code;
    private TextView tv_name;
    private TextView tv_value;
    private TextView tv_state;

    public PrePreferencesViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_preferences_layout);
        tv_code = $(R.id.tv_code);
        tv_name = $(R.id.tv_name);
        tv_value = $(R.id.tv_value);
        tv_state = $(R.id.tv_state);
    }

    @Override
    public void setData(PrePreferencesInfo data) {
        super.setData(data);
        tv_code.setText(data.getCode());
        tv_name.setText(data.getName());
        tv_value.setText(data.getRemark());
        tv_state.setText(data.getStateContent());
    }
}
