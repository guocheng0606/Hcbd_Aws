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
import android.widget.TextView;

import com.android.hcbd.aws.R;
import com.android.hcbd.aws.entity.LedShowInfo;
import com.android.hcbd.aws.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LedShowSearchActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_begin_time)
    TextView tvBeginTime;
    @BindView(R.id.iv_begin_time)
    ImageView ivBeginTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.iv_end_time)
    ImageView ivEndTime;
    @BindView(R.id.btn_complete)
    Button btnComplete;
    @BindView(R.id.btn_charts)
    Button btnCharts;

    private LedShowInfo searchInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_show_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchInfo = (LedShowInfo) getIntent().getSerializableExtra("searchInfo");
        if (searchInfo != null) {
            etName.setText(searchInfo.getName());
            etContent.setText(searchInfo.getRemark());
            tvBeginTime.setText(String.valueOf(searchInfo.getBeginTime()));
            tvEndTime.setText(String.valueOf(searchInfo.getEndTime()));
        }
        initListener();

    }

    private void initListener() {
        tvBeginTime.setOnClickListener(this);
        ivBeginTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        ivEndTime.setOnClickListener(this);
        btnComplete.setOnClickListener(this);
        btnCharts.setOnClickListener(this);
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
                if (!TextUtils.isEmpty(beginStr))
                    beginDate.set(Integer.parseInt(beginStr.substring(0, 4)), Integer.parseInt(beginStr.substring(5, 7)) - 1, Integer.parseInt(beginStr.substring(8, 10))
                            , Integer.parseInt(beginStr.substring(11, 13)), Integer.parseInt(beginStr.substring(14, 16)));
                showTimePickerDialog(tvBeginTime, beginDate);
                break;
            case R.id.tv_end_time:
            case R.id.iv_end_time:
                Calendar endDate = Calendar.getInstance();
                String endStr = tvEndTime.getText().toString();
                if (!TextUtils.isEmpty(endStr))
                    endDate.set(Integer.parseInt(endStr.substring(0, 4)), Integer.parseInt(endStr.substring(5, 7)) - 1, Integer.parseInt(endStr.substring(8, 10))
                            , Integer.parseInt(endStr.substring(11, 13)), Integer.parseInt(endStr.substring(14, 16)));
                showTimePickerDialog(tvEndTime, endDate);
                break;
            case R.id.btn_complete:
                if (searchInfo == null)
                    searchInfo = new LedShowInfo();
                searchInfo.setName(etName.getText().toString());
                searchInfo.setRemark(etContent.getText().toString());
                searchInfo.setBeginTime(tvBeginTime.getText().toString());
                searchInfo.setEndTime(tvEndTime.getText().toString());
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setEventId(MessageEvent.EVENT_LEDSHOW_SEARCH);
                messageEvent.setObj(searchInfo);
                EventBus.getDefault().post(messageEvent);
                finishActivity();
                break;
            case R.id.btn_charts:
                Intent intent = new Intent(LedShowSearchActivity.this,CarNoticeChartsActivity.class);
                intent.putExtra("start_time",tvBeginTime.getText().toString());
                intent.putExtra("end_time",tvEndTime.getText().toString());
                startActivity(intent);
                break;
        }
    }

}
