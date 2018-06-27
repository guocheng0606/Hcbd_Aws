package com.android.hcbd.aws.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.hcbd.aws.R;
import com.android.hcbd.aws.entity.CarTypeInfo;
import com.android.hcbd.aws.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarTypeSearchActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_axles_num)
    EditText etAxlesNum;
    @BindView(R.id.btn_complete)
    Button btnComplete;
    private CarTypeInfo searchInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_type_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchInfo = (CarTypeInfo) getIntent().getSerializableExtra("searchInfo");
        if(null != searchInfo){
            etName.setText(searchInfo.getName());
            etAxlesNum.setText(searchInfo.getAxisNum());
        }
        btnComplete.setOnClickListener(this);

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
        switch (view.getId()){
            case R.id.btn_complete:
                if(searchInfo == null)
                    searchInfo = new CarTypeInfo();
                searchInfo.setName(etName.getText().toString());
                searchInfo.setAxisNum(etAxlesNum.getText().toString());
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setEventId(MessageEvent.EVENT_CARTYPE_SEARCH);
                messageEvent.setObj(searchInfo);
                EventBus.getDefault().post(messageEvent);
                finishActivity();
                break;
        }
    }
}
