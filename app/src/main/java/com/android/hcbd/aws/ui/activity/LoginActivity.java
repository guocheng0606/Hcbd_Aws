package com.android.hcbd.aws.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import com.android.hcbd.aws.MyApplication;
import com.android.hcbd.aws.R;
import com.android.hcbd.aws.entity.LoginInfo;
import com.android.hcbd.aws.util.HttpUrlUtils;
import com.android.hcbd.aws.util.LogUtils;
import com.android.hcbd.aws.util.ProgressDialogUtils;
import com.android.hcbd.aws.util.SharedPreferencesUtil;
import com.android.hcbd.aws.util.ToastUtils;
import com.blankj.utilcode.util.BarUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity{

    private static final int REQUEST_CODE_PERMISSION = 101;
    private static final int REQUEST_CODE_SETTING = 300;

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        BarUtils.setTransparent(this);
        checkUpdate();

        String user = SharedPreferencesUtil.get(this, "username_info");
        etUsername.setText(user);
        etUsername.setSelection(user.length());

        requestPermission();
    }

    private void checkUpdate() {
        PgyUpdateManager.register(this, this.getPackageName() + ".provider", new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {

            }

            @Override
            public void onUpdateAvailable(String result) {
                // 将新版本信息封装到AppBean中
                final AppBean appBean = getAppBeanFromString(result);

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("发现新版本，v"+appBean.getVersionName());
                builder.setMessage(appBean.getReleaseNote());
                builder.setCancelable(false);
                /*builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });*/
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        startDownloadTask(LoginActivity.this,appBean.getDownloadURL());
                    }
                });
                builder.create().show();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PgyUpdateManager.unregister();
        OkGo.getInstance().cancelTag(this);
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        if(TextUtils.isEmpty(etUsername.getText().toString())){
            ToastUtils.showShortToast(this,"请输入用户名");
            return;
        }
        if(TextUtils.isEmpty(etPassword.getText().toString())){
            ToastUtils.showShortToast(this,"请输入密码");
            return;
        }
        OkGo.<String>post(MyApplication.getInstance().getBsaeUrl()+HttpUrlUtils.login_url)
                .tag(this)
                .retryCount(0)
                .params("userName",etUsername.getText().toString())
                .params("userPwd",etPassword.getText().toString())
                .params("orgCode","027")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        ProgressDialogUtils.showLoading(LoginActivity.this);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body();
                        LogUtils.LogShow(result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            if(!TextUtils.isEmpty(jsonObject.getString("data"))){
                                ToastUtils.showShortToast(LoginActivity.this,"登录成功！");

                                LoginInfo loginInfo = new LoginInfo();
                                loginInfo.setToken(jsonObject.getString("token"));

                                Gson gson = new Gson();
                                LoginInfo.UserInfo userInfo = gson.fromJson(jsonObject.getString("data"),LoginInfo.UserInfo.class);
                                loginInfo.setUserInfo(userInfo);

                                JSONArray array = new JSONArray(jsonObject.getString("menuList"));
                                List<LoginInfo.menuInfo> menuInfoList = new ArrayList<LoginInfo.menuInfo>();
                                for(int i=0;i<array.length();i++){
                                    LoginInfo.menuInfo menuInfo = gson.fromJson(array.getString(i),LoginInfo.menuInfo.class);
                                    menuInfoList.add(menuInfo);
                                }
                                loginInfo.setMenuList(menuInfoList);

                                SharedPreferencesUtil.save(LoginActivity.this, "login_info", gson.toJson(loginInfo));
                                SharedPreferencesUtil.save(LoginActivity.this, "username_info", etUsername.getText().toString());
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            try {
                                if(!TextUtils.isEmpty(jsonObject.getString("error"))){
                                    ToastUtils.showShortToast(LoginActivity.this,jsonObject.getString("error"));
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("提示");
                        builder.setMessage("连接服务器失败，是否需要修改服务器地址");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(LoginActivity.this,IpAddressActivity.class);
                                startActivity(intent);
                            }
                        });
                        builder.create().show();

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressDialogUtils.dismissLoading();
                    }
                });
    }

    private void requestPermission(){
        AndPermission.with(this)
                .requestCode(REQUEST_CODE_PERMISSION)
                .permission(Permission.STORAGE)
                .callback(permissionListener)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        //AndPermission.rationaleDialog(LoginActivity.this, rationale).show();
                        // 自定义对话框
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("权限申请")
                                .setMessage("APP需要获取读写存储卡权限，以正常使用相关功能")
                                .setPositiveButton("好，给你", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        rationale.resume();
                                    }
                                })
                                .setNegativeButton("我拒绝", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        rationale.cancel();
                                    }
                                }).show();
                    }
                })
                .start();
    }



    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if(requestCode == REQUEST_CODE_PERMISSION) {
                LogUtils.LogShow("successfully");
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if(requestCode == REQUEST_CODE_PERMISSION) {
                LogUtils.LogShow("failure");
                // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
                if (AndPermission.hasAlwaysDeniedPermission(LoginActivity.this, deniedPermissions)) {
                    // 第一种：用默认的提示语。
                    //AndPermission.defaultSettingDialog(LoginActivity.this, REQUEST_CODE_SETTING).show();
                    // 第二种：用自定义的提示语。
                    AndPermission.defaultSettingDialog(LoginActivity.this, REQUEST_CODE_SETTING)
                            .setTitle("权限申请")
                            .setMessage("在设置-应用-高速超限-权限中开启读写存储卡权限，以正常使用相关功能")
                            .setPositiveButton("去设置")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SETTING: {
                LogUtils.LogShow("The user came back from the settings");
                if(AndPermission.hasPermission(LoginActivity.this, Permission.STORAGE)) {
                    //执行拥有权限时的下一步。
                } else {
                    // 使用AndPermission提供的默认设置dialog，用户点击确定后会打开App的设置页面让用户授权。
                    //AndPermission.defaultSettingDialog(this, requestCode).show();
                    // 建议：自定义这个Dialog，提示具体需要开启什么权限，自定义Dialog具体实现上面有示例代码。
                    AndPermission.defaultSettingDialog(LoginActivity.this, requestCode)
                            .setTitle("权限申请")
                            .setMessage("在设置-应用-高速超限-权限中开启读写存储卡权限，以正常使用相关功能")
                            .setPositiveButton("去设置")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                }
                break;
            }
        }
    }

}
