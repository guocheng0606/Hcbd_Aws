package com.android.hcbd.aws.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.hcbd.aws.MyApplication;
import com.android.hcbd.aws.R;
import com.android.hcbd.aws.entity.PreCheckDataInfo;
import com.android.hcbd.aws.event.MessageEvent;
import com.android.hcbd.aws.ui.activity.ImagePreActivity;
import com.android.hcbd.aws.util.HttpUrlUtils;
import com.android.hcbd.aws.util.LogUtils;
import com.android.hcbd.aws.util.ToastUtils;
import com.android.hcbd.aws.viewholder.PreCheckDataViewHolder;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 预检
 */
public class PreCheckFragment extends Fragment implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.ll_video_img)
    LinearLayout llVideoImg;
    @BindView(R.id.iv_preVideo)
    ImageView ivPreVideo;
    @BindView(R.id.ll_header)
    LinearLayout llHeader;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.layout_power)
    RelativeLayout layoutPower;
    @BindView(R.id.tv_power)
    TextView tvPower;

    private String minWeight = "";
    private String carNo = "";
    private String beginTime = "";
    private boolean isOver = false;

    private RecyclerArrayAdapter<PreCheckDataInfo> adapter;

    private boolean isHidden;

    Unbinder unbinder;

    private String mParam1;
    private String mParam2;

    public PreCheckFragment() {
    }

    public static PreCheckFragment newInstance(String param1, String param2) {
        PreCheckFragment fragment = new PreCheckFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pre_check, container, false);
        unbinder = ButterKnife.bind(this, view);

        llVideoImg.setVisibility(View.GONE);

        if (TextUtils.isEmpty(MyApplication.getInstance().getPowerStr("首页"))) {
            swipeRefreshLayout.setVisibility(View.GONE);
            layoutPower.setVisibility(View.VISIBLE);
            tvPower.setText("您没有访问首页权限,请与管理员联系！");
        } else {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            layoutPower.setVisibility(View.GONE);
            initView();
            initHttpData();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MyApplication.getInstance().getMyService().preFlag = true;
                    MyApplication.getInstance().getMyService().sendPreData();
                }
            }, 1000);
            initListener();
        }

        return view;
    }

    private void initListener() {
        ivFilter.setOnClickListener(this);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), ImagePreActivity.class);
                intent.putExtra("type", "1");
                intent.putExtra("image_info", adapter.getItem(position));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade, R.anim.hold);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getEventId()) {
            case MessageEvent.EVENT_GETPREDATA_THREAD:
                System.out.println("goc-pre列表刷新");
                httpPreCheckData(1);
                break;
            case MessageEvent.EVENT_DATA_THREAD:
                if (!isHidden)
                    MyApplication.getInstance().getMyService().preFlag = true;
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerDecoration itemDecoration = new DividerDecoration(0xFFEDEDED, 1, 0, 0);
        itemDecoration.setDrawLastItem(true);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapterWithProgress(adapter = new RecyclerArrayAdapter<PreCheckDataInfo>(getActivity()) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new PreCheckDataViewHolder(parent);
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

    private void initHttpData() {
        OkGo.<String>post(MyApplication.getInstance().getBsaeUrl() + HttpUrlUtils.pre_check_to_list_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
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
                            minWeight = jsonObject.getString("minWeight");
                            httpPreCheckData(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.showShortToast(MyApplication.getInstance(), "连接服务器异常");
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });

    }

    private void httpPreCheckData(final int type) {
        OkGo.<String>post(MyApplication.getInstance().getBsaeUrl() + HttpUrlUtils.pre_check_list_url)
                .tag(this)
                .retryCount(0)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("weight", minWeight)
                .params("queryTime", type == 0 ? "" : (adapter.getAllData().size() > 0 ? adapter.getAllData().get(0).getCreateTime() : ""))
                .params("carNo", carNo)
                .params("beginTime", beginTime)
                .params("isOver", isOver ? 1 : 0)
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
                            Gson gson = new Gson();
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                            if (jsonArray.length() > 0) {
                                List<PreCheckDataInfo> list = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    PreCheckDataInfo preCheckDataInfo = gson.fromJson(jsonArray.getString(i), PreCheckDataInfo.class);
                                    list.add(preCheckDataInfo);
                                }

                                if (adapter.getAllData().size() > 0 && type == 1) {
                                    adapter.insertAll(list, 0);
                                } else {
                                    adapter.clear();
                                    adapter.addAll(list);
                                }

                                /*Glide.with(getActivity())
                                        .load(HttpUrlUtils.BASEURL+preCheckDataInfoList.get(0).getImg())
                                        .into(ivPreVideo);*/
                                //llVideoImg.setVisibility(View.VISIBLE);
                            } else {
                                if (adapter.getAllData().size() > 0) {

                                } else {
                                    adapter.clear();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

    }

    @Override
    public void onRefresh() {
        MyApplication.getInstance().getMyService().preFlag = false;
        httpPreCheckData(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MyApplication.getInstance().getMyService().preFlag = true;
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        System.out.println("加载更多。。。");
        adapter.stopMore();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_filter:
                showFilterPopup();
                break;
        }
    }

    private void showFilterPopup() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(R.layout.dialog_pre_check_filter_layout);

        final EditText et_carNO = (EditText) window.findViewById(R.id.et_carNO);
        ImageView iv_carNo = (ImageView) window.findViewById(R.id.iv_carNo);
        final TextView tv_end_time = (TextView) window.findViewById(R.id.tv_end_time);
        ImageView iv_end_time = (ImageView) window.findViewById(R.id.iv_end_time);
        final RadioButton rb_yes = (RadioButton) window.findViewById(R.id.rb_yes);
        final RadioButton rb_no = (RadioButton) window.findViewById(R.id.rb_no);
        Button btn_clear = (Button) window.findViewById(R.id.btn_clear);
        Button btn_cancle = (Button) window.findViewById(R.id.btn_cancle);
        Button btn_ok = (Button) window.findViewById(R.id.btn_ok);
        et_carNO.setText(carNo);
        tv_end_time.setText(beginTime);
        if (isOver)
            rb_yes.setChecked(true);
        else
            rb_no.setChecked(true);
        tv_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar date = Calendar.getInstance();
                String beginStr = tv_end_time.getText().toString();
                if(!TextUtils.isEmpty(beginStr))
                    date.set(2000,0,01,Integer.parseInt(beginStr.substring(0,2)),Integer.parseInt(beginStr.substring(3,5)));
                showTimePickerDialog(tv_end_time,date);
            }
        });
        iv_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar date = Calendar.getInstance();
                String beginStr = tv_end_time.getText().toString();
                if(!TextUtils.isEmpty(beginStr))
                    date.set(2000,0,01,Integer.parseInt(beginStr.substring(0,2)),Integer.parseInt(beginStr.substring(3,5)));
                showTimePickerDialog(tv_end_time,date);
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_carNO.setText("");
                tv_end_time.setText("");
                rb_no.setChecked(true);
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().getMyService().preFlag = false;
                carNo = et_carNO.getText().toString();
                beginTime = tv_end_time.getText().toString();
                if (rb_yes.isChecked())
                    isOver = true;
                else
                    isOver = false;
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
                dialog.dismiss();
            }
        });

    }

    private void showTimePickerDialog(final TextView tv,Calendar selectedDate) {
        new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                tv.setText(format.format(date));
            }
        }).setType(new boolean[]{false, false, false, true, true, false})
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText("选择时间")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(0xFFEFEFEF)//标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                .isCenterLabel(false)
                .isDialog(true)//是否显示为对话框样式
                .build().show();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if (hidden) {
            System.out.println("as = PreCheckFragment+当前不可见");
            MyApplication.getInstance().getMyService().preFlag = false;
        } else {
            System.out.println("as = PreCheckFragment+当前可见");
            MyApplication.getInstance().getMyService().preFlag = true;
        }
    }
}
