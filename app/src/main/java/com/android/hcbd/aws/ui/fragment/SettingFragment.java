package com.android.hcbd.aws.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.hcbd.aws.MyApplication;
import com.android.hcbd.aws.R;
import com.android.hcbd.aws.event.MessageEvent;
import com.android.hcbd.aws.ui.activity.CarTypeListActivity;
import com.android.hcbd.aws.ui.activity.IpAddressActivity;
import com.android.hcbd.aws.ui.activity.LedShowActivity;
import com.android.hcbd.aws.ui.activity.PreferencesActivity;
import com.android.hcbd.aws.ui.activity.UpdatePasswordActivity;
import com.android.hcbd.aws.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 设置
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.ll_preferences)
    LinearLayout llPreferences;
    @BindView(R.id.ll_update_password)
    LinearLayout llUpdatePassword;
    @BindView(R.id.ll_led_show)
    LinearLayout llLedShow;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    Unbinder unbinder;
    @BindView(R.id.ll_ipAddress)
    LinearLayout llIpAddress;
    @BindView(R.id.ll_car_type)
    LinearLayout llCarType;

    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        unbinder = ButterKnife.bind(this, view);

        llPreferences.setOnClickListener(this);
        llUpdatePassword.setOnClickListener(this);
        llLedShow.setOnClickListener(this);
        llIpAddress.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        llCarType.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_preferences:
                if (TextUtils.isEmpty(MyApplication.getInstance().getPowerStr("预检参数"))) {
                    ToastUtils.showShortToast(MyApplication.getInstance(), "您没有访问预检参数权限,请与管理员联系！");
                    return;
                }
                startActivity(new Intent(getActivity(), PreferencesActivity.class));
                break;
            case R.id.ll_update_password:
                startActivity(new Intent(getActivity(), UpdatePasswordActivity.class));
                break;
            case R.id.ll_led_show:
                if (TextUtils.isEmpty(MyApplication.getInstance().getPowerStr("LED显示"))) {
                    ToastUtils.showShortToast(MyApplication.getInstance(), "您没有访问LED显示权限,请与管理员联系！");
                    return;
                }
                startActivity(new Intent(getActivity(), LedShowActivity.class));
                break;
            case R.id.ll_ipAddress:
                startActivity(new Intent(getActivity(), IpAddressActivity.class));
                break;
            case R.id.ll_car_type:
                if (TextUtils.isEmpty(MyApplication.getInstance().getPowerStr("车型信息"))) {
                    ToastUtils.showShortToast(MyApplication.getInstance(), "您没有访问车型信息权限,请与管理员联系！");
                    return;
                }
                startActivity(new Intent(getActivity(), CarTypeListActivity.class));
                break;
            case R.id.btn_logout:
                logout();
                break;
        }
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("您确认退出登录吗？");
        builder.setCancelable(false);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setEventId(MessageEvent.EVENT_LOGINOUT);
                EventBus.getDefault().post(messageEvent);
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
}
