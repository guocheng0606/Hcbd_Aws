package com.android.hcbd.aws.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.hcbd.aws.MyApplication;
import com.android.hcbd.aws.R;
import com.android.hcbd.aws.entity.CarTypeInfo;
import com.android.hcbd.aws.event.MessageEvent;
import com.android.hcbd.aws.util.HttpUrlUtils;
import com.android.hcbd.aws.util.LogUtils;
import com.android.hcbd.aws.util.ProgressDialogUtils;
import com.android.hcbd.aws.util.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarTypeEditActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_axles_num)
    EditText etAxlesNum;
    @BindView(R.id.et_pre_check_value)
    EditText etPreCheckValue;
    @BindView(R.id.et_check_value)
    EditText etCheckValue;
    @BindView(R.id.cb_yes1)
    CheckBox cbYes1;
    @BindView(R.id.cb_no1)
    CheckBox cbNo1;
    @BindView(R.id.cb_yes2)
    CheckBox cbYes2;
    @BindView(R.id.cb_no2)
    CheckBox cbNo2;
    @BindView(R.id.btn_complete)
    Button btnComplete;

    private CarTypeInfo carTypeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_type_edit);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        carTypeInfo = (CarTypeInfo) getIntent().getSerializableExtra("data");

        tvTitle.setText("车型信息录入");
        cbYes1.setChecked(false);
        cbNo1.setChecked(true);
        cbYes2.setChecked(true);
        cbNo2.setChecked(false);

        if(null != carTypeInfo){
            tvTitle.setText("车型信息编辑");
            etName.setText(carTypeInfo.getName());
            etAxlesNum.setText(carTypeInfo.getAxisNum());
            etPreCheckValue.setText(""+carTypeInfo.getLimit());
            etCheckValue.setText(""+carTypeInfo.getCheckLimit());
            if(carTypeInfo.getIsDef().equals("1")){
                cbYes1.setChecked(true);
                cbNo1.setChecked(false);
            }else{
                cbYes1.setChecked(false);
                cbNo1.setChecked(true);
            }
            if(carTypeInfo.getIsShow().equals("1")){
                cbYes2.setChecked(true);
                cbNo2.setChecked(false);
            }else{
                cbYes2.setChecked(false);
                cbNo2.setChecked(true);
            }
        }

        String[] strs = MyApplication.getInstance().getPowerStr("车型信息").split(",");
        if(strs.length == 4){
            if(strs[2].equals("0")){
                btnComplete.setEnabled(false);
                btnComplete.setBackgroundResource(R.drawable.shape_check_data_button_06);
            }
        }

        initListener();
    }

    private void initListener() {
        cbYes1.setOnClickListener(this);
        cbNo1.setOnClickListener(this);
        cbYes2.setOnClickListener(this);
        cbNo2.setOnClickListener(this);
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
            case R.id.cb_yes1:
                cbYes1.setChecked(true);
                cbNo1.setChecked(false);
                break;
            case R.id.cb_no1:
                cbYes1.setChecked(false);
                cbNo1.setChecked(true);
                break;
            case R.id.cb_yes2:
                cbYes2.setChecked(true);
                cbNo2.setChecked(false);
                break;
            case R.id.cb_no2:
                cbYes2.setChecked(false);
                cbNo2.setChecked(true);
                break;
            case R.id.btn_complete:
                if(TextUtils.isEmpty(etName.getText().toString())){
                    ToastUtils.showShortToast(MyApplication.getInstance(),"请输入车型");
                    return;
                }
                if(TextUtils.isEmpty(etAxlesNum.getText().toString())){
                    ToastUtils.showShortToast(MyApplication.getInstance(),"请输入轴数");
                    return;
                }
                if(TextUtils.isEmpty(etPreCheckValue.getText().toString())){
                    ToastUtils.showShortToast(MyApplication.getInstance(),"请输入预检提示值");
                    return;
                }
                if(TextUtils.isEmpty(etCheckValue.getText().toString())){
                    ToastUtils.showShortToast(MyApplication.getInstance(),"请输入精检限值");
                    return;
                }
                httpComplete();
                break;
        }
    }

    private void httpComplete() {
        OkGo.<String>post(MyApplication.getInstance().getBsaeUrl()+ HttpUrlUtils.car_type_edit_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("oid", carTypeInfo == null ? "" : "" + carTypeInfo.getId())
                .params("id", carTypeInfo == null ? "" : "" + carTypeInfo.getId())
                .params("code", carTypeInfo == null ? "" : "" + carTypeInfo.getCode())
                .params("name", etName.getText().toString())
                .params("axisNum", etAxlesNum.getText().toString())
                .params("limit", etPreCheckValue.getText().toString())
                .params("checkLimit", etCheckValue.getText().toString())
                .params("isDef", cbYes1.isChecked()? "1":"0")
                .params("isShow", cbYes2.isChecked()? "1":"0")
                .params("state", "1")
                .params("operNames", MyApplication.getInstance().getLoginInfo().getUserInfo().getNames())
                .params("orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        ProgressDialogUtils.showLoading(CarTypeEditActivity.this);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body();
                        LogUtils.LogShow(result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            if (!TextUtils.isEmpty(jsonObject.getString("data"))) {
                                ToastUtils.showShortToast(MyApplication.getInstance(), jsonObject.getString("data"));
                                MessageEvent messageEvent = new MessageEvent();
                                messageEvent.setEventId(MessageEvent.EVENT_CARTYPE_EDIT_SUCCESS);
                                EventBus.getDefault().post(messageEvent);
                                finishActivity();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            try {
                                if (!TextUtils.isEmpty(jsonObject.getString("error")))
                                    ToastUtils.showShortToast(MyApplication.getInstance(), jsonObject.getString("error"));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.showLongToast(MyApplication.getInstance(), "连接服务器异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressDialogUtils.dismissLoading();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //根据 Tag 取消请求
        OkGo.getInstance().cancelTag(this);
    }

}
