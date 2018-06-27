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
import com.android.hcbd.aws.entity.PrePreferencesInfo;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreferencesEditActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_value)
    EditText etValue;
    @BindView(R.id.cb_enable)
    CheckBox cbEnable;
    @BindView(R.id.cb_freeze)
    CheckBox cbFreeze;
    @BindView(R.id.cb_close)
    CheckBox cbClose;
    @BindView(R.id.btn_complete)
    Button btnComplete;
    @BindView(R.id.et_code)
    EditText etCode;

    private PrePreferencesInfo prePreferencesInfo;
    private List<String> codes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences_edit);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prePreferencesInfo = (PrePreferencesInfo) getIntent().getSerializableExtra("data");
        codes = getIntent().getStringArrayListExtra("codes");

        cbEnable.setChecked(true);
        cbFreeze.setChecked(false);
        cbClose.setChecked(false);
        if (prePreferencesInfo == null) {
            tvTitle.setText("添加");
            etCode.setEnabled(true);
        } else {
            tvTitle.setText("编辑");
            etCode.setEnabled(false);
            etCode.setText(prePreferencesInfo.getCode());
            etName.setText(prePreferencesInfo.getName());
            etValue.setText(prePreferencesInfo.getRemark());
            if (prePreferencesInfo.getState().equals("1")) {
                cbEnable.setChecked(true);
                cbFreeze.setChecked(false);
                cbClose.setChecked(false);
            } else if (prePreferencesInfo.getState().equals("2")) {
                cbEnable.setChecked(false);
                cbFreeze.setChecked(true);
                cbClose.setChecked(false);
            } else {
                cbEnable.setChecked(false);
                cbFreeze.setChecked(false);
                cbClose.setChecked(true);
            }
        }
        String[] strs = MyApplication.getInstance().getPowerStr("预检参数").split(",");
        if(strs.length == 4){
            if(strs[2].equals("0")){
                btnComplete.setEnabled(false);
                btnComplete.setBackgroundResource(R.drawable.shape_check_data_button_06);
            }
        }

        initListener();
    }


    private void initListener() {
        cbEnable.setOnClickListener(this);
        cbFreeze.setOnClickListener(this);
        cbClose.setOnClickListener(this);
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
        switch (view.getId()) {
            case R.id.cb_enable:
                cbEnable.setChecked(true);
                cbFreeze.setChecked(false);
                cbClose.setChecked(false);
                break;
            case R.id.cb_freeze:
                cbEnable.setChecked(false);
                cbFreeze.setChecked(true);
                cbClose.setChecked(false);
                break;
            case R.id.cb_close:
                cbEnable.setChecked(false);
                cbFreeze.setChecked(false);
                cbClose.setChecked(true);
                break;
            case R.id.btn_complete:
                /*String[] strs = MyApplication.getInstance().getPowerStr("预检参数").split(",");
                if(strs.length != 4)
                    return;
                if(strs[2].equals("0")){
                    ToastUtils.showShortToast(MyApplication.getInstance(), "当前登录的账户没有该权限，请与管理员联系");
                    return;
                }*/

                if(prePreferencesInfo == null){
                    if(TextUtils.isEmpty(etCode.getText().toString())){
                        ToastUtils.showShortToast(MyApplication.getInstance(),"请输入编码");
                        return;
                    }
                    if(codes == null)
                        return;
                    boolean flag = false;
                    for(String str:codes){
                        if(str.equals(etCode.getText().toString())){
                            flag = true;
                            break;
                        }
                    }
                    if(flag){
                        ToastUtils.showShortToast(MyApplication.getInstance(),"当前编码已存在，请重新输入");
                        return;
                    }
                    httpComplate();
                }else{
                    httpComplate();
                }
                break;
        }
    }

    private void httpComplate() {
        OkGo.<String>post(MyApplication.getInstance().getBsaeUrl()+HttpUrlUtils.check_setting_edit_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("oid", prePreferencesInfo == null ? "" : "" + prePreferencesInfo.getId())
                .params("params", "A001")
                .params("type", "A001")
                .params("code", prePreferencesInfo == null ? etCode.getText().toString() : prePreferencesInfo.getCode())
                .params("name", etName.getText().toString())
                .params("remark", etValue.getText().toString())
                .params("state", cbEnable.isChecked() ? 1 : (cbFreeze.isChecked() ? 2 : 3))
                .params("operNames", MyApplication.getInstance().getLoginInfo().getUserInfo().getNames())
                .params("id", prePreferencesInfo == null ? "" : "" + prePreferencesInfo.getId())
                .params("orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("typeNote", "A001")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        ProgressDialogUtils.showLoading(PreferencesEditActivity.this);
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
                                messageEvent.setEventId(MessageEvent.EVENT_EDIT_PREFERENCES_SUCCESS);
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
