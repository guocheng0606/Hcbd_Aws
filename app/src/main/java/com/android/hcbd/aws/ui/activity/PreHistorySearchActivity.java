package com.android.hcbd.aws.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.hcbd.aws.R;
import com.android.hcbd.aws.entity.PreHistoryInfo;
import com.android.hcbd.aws.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreHistorySearchActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_begin_time)
    TextView tvBeginTime;
    @BindView(R.id.iv_begin_time)
    ImageView ivBeginTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.iv_end_time)
    ImageView ivEndTime;
    @BindView(R.id.et_lane)
    EditText etLane;
    @BindView(R.id.et_plate_number)
    EditText etPlateNumber;
    @BindView(R.id.et_axles_num)
    EditText etAxlesNum;
    @BindView(R.id.et_tonnage_range)
    EditText etTonnageRange;
    @BindView(R.id.et_range_to)
    EditText etRangeTo;
    @BindView(R.id.btn_complete)
    Button btnComplete;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;
    @BindView(R.id.btn_vehicle_flow)
    Button btnVehicleFlow;

    private PreHistoryInfo searchInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_history_search);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchInfo = (PreHistoryInfo) getIntent().getSerializableExtra("searchInfo");
        rbNo.setChecked(true);
        if (searchInfo != null) {
            tvBeginTime.setText("" + searchInfo.getBeginTime());
            tvEndTime.setText("" + searchInfo.getEndTime());
            etLane.setText(searchInfo.getLane());
            etPlateNumber.setText(searchInfo.getCarNo());
            etAxlesNum.setText(searchInfo.getCode());
            etTonnageRange.setText("" + searchInfo.getBeginAmt());
            etRangeTo.setText("" + searchInfo.getEndAmt());
            if (searchInfo.getIsOver().equals("1"))
                rbYes.setChecked(true);
            else
                rbNo.setChecked(true);

        }
        initListener();
    }

    private void initListener() {
        tvBeginTime.setOnClickListener(this);
        ivBeginTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        ivEndTime.setOnClickListener(this);
        btnComplete.setOnClickListener(this);
        btnVehicleFlow.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_begin_time:
            case R.id.iv_begin_time:
                Calendar beginDate = Calendar.getInstance();
                String beginStr = tvBeginTime.getText().toString();
                if(!TextUtils.isEmpty(beginStr))
                    beginDate.set(Integer.parseInt(beginStr.substring(0,4)),Integer.parseInt(beginStr.substring(5,7))-1,Integer.parseInt(beginStr.substring(8,10))
                            ,Integer.parseInt(beginStr.substring(11,13)),Integer.parseInt(beginStr.substring(14,16)));
                showTimePickerDialog(tvBeginTime,beginDate);
                break;
            case R.id.tv_end_time:
            case R.id.iv_end_time:
                Calendar endDate = Calendar.getInstance();
                String endStr = tvEndTime.getText().toString();
                if(!TextUtils.isEmpty(endStr))
                    endDate.set(Integer.parseInt(endStr.substring(0,4)),Integer.parseInt(endStr.substring(5,7))-1,Integer.parseInt(endStr.substring(8,10))
                            ,Integer.parseInt(endStr.substring(11,13)),Integer.parseInt(endStr.substring(14,16)));
                showTimePickerDialog(tvEndTime,endDate);
                break;
            case R.id.btn_complete:
                if (searchInfo == null)
                    searchInfo = new PreHistoryInfo();
                searchInfo.setBeginTime(tvBeginTime.getText().toString());
                searchInfo.setEndTime(tvEndTime.getText().toString());
                searchInfo.setLane(etLane.getText().toString());
                searchInfo.setCarNo(etPlateNumber.getText().toString());
                searchInfo.setCode(etAxlesNum.getText().toString());
                searchInfo.setBeginAmt(etTonnageRange.getText().toString());
                searchInfo.setEndAmt(etRangeTo.getText().toString());
                searchInfo.setIsOver(rbYes.isChecked() ? "1" : "0");
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setEventId(MessageEvent.EVENT_PRECHECK_HISTORY_SEARCH);
                messageEvent.setObj(searchInfo);
                EventBus.getDefault().post(messageEvent);
                finishActivity();
                break;
            case R.id.btn_vehicle_flow:
                Intent intent = new Intent(PreHistorySearchActivity.this,CarFlowChartsActivity.class);
                intent.putExtra("start_time",tvBeginTime.getText().toString());
                intent.putExtra("end_time",tvEndTime.getText().toString());
                startActivity(intent);
                break;
        }
    }

}
