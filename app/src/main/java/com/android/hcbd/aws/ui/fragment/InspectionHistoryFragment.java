package com.android.hcbd.aws.ui.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.hcbd.aws.MyApplication;
import com.android.hcbd.aws.R;
import com.android.hcbd.aws.entity.CheckDataInfo;
import com.android.hcbd.aws.event.MessageEvent;
import com.android.hcbd.aws.ui.activity.InspectionHistorySearchActivity;
import com.android.hcbd.aws.util.HttpUrlUtils;
import com.android.hcbd.aws.util.IntentUtils;
import com.android.hcbd.aws.util.LogUtils;
import com.android.hcbd.aws.util.ToastUtils;
import com.android.hcbd.aws.viewholder.CheckDataHistoryViewHolder;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 精检历史
 */
public class InspectionHistoryFragment extends Fragment implements View.OnClickListener, RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    Unbinder unbinder;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.layout_power)
    RelativeLayout layoutPower;
    @BindView(R.id.tv_power)
    TextView tvPower;
    @BindView(R.id.iv_export)
    ImageView ivExport;

    private int currentPage = 1;
    private RecyclerArrayAdapter<CheckDataInfo> adapter;
    private List<CheckDataInfo> checkDataInfoList = new ArrayList<>();

    private CheckDataInfo searchInfo;

    private String mParam1;
    private String mParam2;

    public InspectionHistoryFragment() {
        // Required empty public constructor
    }

    public static InspectionHistoryFragment newInstance(String param1, String param2) {
        InspectionHistoryFragment fragment = new InspectionHistoryFragment();
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
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inspection_history, container, false);

        unbinder = ButterKnife.bind(this, view);

        initView();
        if (TextUtils.isEmpty(MyApplication.getInstance().getPowerStr("精检历史"))) {
            swipeRefreshLayout.setVisibility(View.GONE);
            layoutPower.setVisibility(View.VISIBLE);
            tvPower.setText("您没有访问精检历史权限,请与管理员联系！");
        } else {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            layoutPower.setVisibility(View.GONE);
            if (null == searchInfo)
                searchInfo = new CheckDataInfo();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String strTime = formatter.format(curDate);
            searchInfo.setBeginTime(strTime + " 00:00");
            searchInfo.setEndTime(strTime + " 23:59");
            searchInfo.setBeginAmt("");
            searchInfo.setEndAmt("");
            searchInfo.setIsCheck("0");
            initHttpData();
        }
        ivSearch.setOnClickListener(this);
        ivExport.setOnClickListener(this);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getEventId()) {
            case MessageEvent.EVENT_INSPECTION_HISTORY_SEARCH:
                searchInfo = (CheckDataInfo) event.getObj();
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
                break;
        }
    }

    private void initHttpData() {
        OkGo.<String>post(MyApplication.getInstance().getBsaeUrl() + HttpUrlUtils.check_list_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("page.currentPage", currentPage)

                .params("beginTime", searchInfo == null ? "" : searchInfo.getBeginTime())
                .params("endTime", searchInfo == null ? "" : searchInfo.getEndTime())
                .params("lane", searchInfo == null ? "" : searchInfo.getLane())
                .params("carNo", searchInfo == null ? "" : searchInfo.getCarNo())
                .params("carType.axisNum", searchInfo == null ? "" : searchInfo.getCode())
                .params("goods", searchInfo == null ? "" : searchInfo.getGoods())
                .params("beginAmt", searchInfo == null ? "" : "" + searchInfo.getBeginAmt())
                .params("endAmt", searchInfo == null ? "" : "" + searchInfo.getEndAmt())
                .params("isCheck", searchInfo == null ? "" : "" + searchInfo.getIsCheck())
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body();
                        LogUtils.LogShow(result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                            if (jsonArray.length() > 0) {
                                checkDataInfoList.clear();
                                Gson gson = new Gson();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    CheckDataInfo CheckDataInfo = gson.fromJson(jsonArray.getString(i), CheckDataInfo.class);
                                    checkDataInfoList.add(CheckDataInfo);
                                }
                                if (currentPage == 1)
                                    adapter.clear();
                                adapter.addAll(checkDataInfoList);
                            } else {
                                if (currentPage == 1) {
                                    adapter.clear();
                                } else {
                                    adapter.stopMore();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            adapter.pauseMore();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (currentPage == 1) {
                            ToastUtils.showShortToast(MyApplication.getInstance(), "连接服务器异常");
                        } else {
                            adapter.pauseMore();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerDecoration itemDecoration = new DividerDecoration(0xFFEDEDED, 1, 0, 0);
        itemDecoration.setDrawLastItem(true);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapterWithProgress(adapter = new RecyclerArrayAdapter<CheckDataInfo>(getActivity()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new CheckDataHistoryViewHolder(parent);
            }
        });
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore);
        adapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
            }
        });
        swipeRefreshLayout.setColorSchemeColors(0xFF1191C7);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        initHttpData();
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        System.out.println("加载更多。。。" + currentPage);
        initHttpData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                if (TextUtils.isEmpty(MyApplication.getInstance().getPowerStr("精检历史"))) {
                    ToastUtils.showShortToast(MyApplication.getInstance(), "您没有访问精检历史权限,请与管理员联系！");
                    return;
                }
                Intent intent = new Intent(getActivity(), InspectionHistorySearchActivity.class);
                if (searchInfo != null)
                    intent.putExtra("searchInfo", searchInfo);
                startActivity(intent);
                break;
            case R.id.iv_export:
                if (TextUtils.isEmpty(MyApplication.getInstance().getPowerStr("精检历史"))) {
                    ToastUtils.showShortToast(MyApplication.getInstance(), "您没有访问精检历史权限,请与管理员联系！");
                    return;
                }
                if(AndPermission.hasPermission(getActivity(), Permission.STORAGE)){
                    httpExport();
                }else{
                    AndPermission.defaultSettingDialog(getActivity(), 110)
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

    private void httpExport() {
        OkGo.<File>post(MyApplication.getInstance().getBsaeUrl() + HttpUrlUtils.inspection_history_export_url)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("carNo", searchInfo == null ? "" : searchInfo.getCarNo())
                .params("isCheck", searchInfo == null ? "" : "" + searchInfo.getIsCheck())
                .params("beginTime", searchInfo == null ? "" : "" + searchInfo.getBeginTime())
                .params("endTime", searchInfo == null ? "" : "" + searchInfo.getEndTime())
                .execute(new FileCallback(MyApplication.getInstance().getSDPath()+"/awsapp/download/","精检数据.xlsx") {
                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        super.onStart(request);
                        ToastUtils.showShortToast(getActivity(),"正在导出精检数据...");
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        ToastUtils.showShortToast(getActivity(),"精检数据导出完成，,可以在手机存储/awsapp/download/下查看。");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("精检数据导出完成");
                        builder.setMessage("是否立即打开查看？");
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
                                dialogInterface.dismiss();
                                Intent intent = IntentUtils.getExcelFileIntent(getActivity(),MyApplication.getInstance().getSDPath()+"/awsapp/download/"+"精检数据.xlsx");
                                startActivity(intent);
                            }
                        });
                        builder.create().show();
                        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
                        mBuilder.setContentTitle("下载完成,点击打开。");//设置通知栏标题
                        mBuilder.setContentText("精检数据.xlsx"); //设置通知栏显示内容
                        PendingIntent pendingIntent= PendingIntent.getActivity(getActivity(), 1, IntentUtils.getExcelFileIntent(getActivity(),MyApplication.getInstance().getSDPath()+"/awsapp/download/"+"精检数据.xlsx") , Notification.FLAG_AUTO_CANCEL);
                        mBuilder.setContentIntent(pendingIntent); //设置通知栏点击意图
                        mBuilder.setTicker("下载完成,点击打开。"); //通知首次出现在通知栏，带上升动画效果的
                        mBuilder.setWhen(System.currentTimeMillis());//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                        mBuilder.setPriority(Notification.PRIORITY_DEFAULT); //设置该通知优先级
                        mBuilder.setAutoCancel(true);//设置这个标志当用户单击面板就可以让通知将自动取消
                        mBuilder.setOngoing(false);//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                        mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                        mBuilder.setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON
                        mNotificationManager.notify(1, mBuilder.build());
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        ToastUtils.showLongToast(MyApplication.getInstance(), "连接服务器异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

}
