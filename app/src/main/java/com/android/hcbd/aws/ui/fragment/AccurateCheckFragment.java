package com.android.hcbd.aws.ui.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.TypedValue;
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
import com.android.hcbd.aws.entity.CheckDataInfo;
import com.android.hcbd.aws.event.MessageEvent;
import com.android.hcbd.aws.util.HttpUrlUtils;
import com.android.hcbd.aws.util.LogUtils;
import com.android.hcbd.aws.util.ProgressDialogUtils;
import com.android.hcbd.aws.util.ToastUtils;
import com.android.hcbd.aws.viewholder.CheckDataViewHolder;
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 精检
 */
public class AccurateCheckFragment extends Fragment implements View.OnClickListener, RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.btn_initial_survey)
    Button btnInitialSurvey;
    @BindView(R.id.btn_recheck)
    Button btnRecheck;
    @BindView(R.id.btn_print)
    Button btnPrint;
    @BindView(R.id.btn_pass)
    Button btnPass;
    @BindView(R.id.ll_handle)
    LinearLayout llHandle;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.layout_power)
    RelativeLayout layoutPower;
    @BindView(R.id.tv_power)
    TextView tvPower;

    private int currentPage = 1;
    private RecyclerArrayAdapter<CheckDataInfo> adapter;
    private List<CheckDataInfo> checkDataInfoList = new ArrayList<>();

    private String carNo;
    private String beginTime;
    private String endTime;
    private boolean isCheck = false;

    private List<String> goodsList = new ArrayList<>();
    private List<CheckDataInfo.CarTypeBean> carTypeBeanList = new ArrayList<>();
    private String lane;
    private int carTypeId = -1;

    private String carAmt;

    Unbinder unbinder;

    private String mParam1;
    private String mParam2;

    public AccurateCheckFragment() {
    }

    public static AccurateCheckFragment newInstance(String param1, String param2) {
        AccurateCheckFragment fragment = new AccurateCheckFragment();
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
        View view = inflater.inflate(R.layout.fragment_accurate_check, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (TextUtils.isEmpty(MyApplication.getInstance().getPowerStr("首页"))) {
            swipeRefreshLayout.setVisibility(View.GONE);
            layoutPower.setVisibility(View.VISIBLE);
            tvPower.setText("您没有访问首页权限,请与管理员联系！");
        } else {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            layoutPower.setVisibility(View.GONE);
            initView();
            getHttpEditInfo();
            initHttpData();
            initListener();
            MyApplication.getInstance().getMyService().sendCarData();
            String[] strs = MyApplication.getInstance().getPowerStr("首页").split(",");
            if (strs.length == 4){
                if (strs[0].equals("0")) {
                    btnInitialSurvey.setEnabled(false);
                    btnInitialSurvey.setBackgroundResource(R.drawable.shape_check_data_button_06);
                }
                if (strs[2].equals("0")) {
                    btnRecheck.setEnabled(false);
                    btnRecheck.setBackgroundResource(R.drawable.shape_check_data_button_06);
                }
            }

        }


        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getEventId()) {
            case MessageEvent.EVENT_GETCARDATA_THREAD:
                System.out.println("goc-获取车实时信息");
                httpGetCarInfo();
                break;
            case MessageEvent.EVENT_CARTYPE_EDIT_SUCCESS:
                getHttpEditInfo();
                break;
        }
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
                return new CheckDataViewHolder(parent);
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
        OkGo.<String>post(MyApplication.getInstance().getBsaeUrl() + HttpUrlUtils.check_list_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("page.currentPage", currentPage)
                .params("isCheck", isCheck ? 2 : 1)
                .params("carNo", carNo)
                .params("beginTime", beginTime)
                .params("endTime", endTime)
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
                                //llHandle.setVisibility(View.VISIBLE);
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

    private void initListener() {
        btnInitialSurvey.setOnClickListener(this);
        btnRecheck.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
        btnPass.setOnClickListener(this);
        ivFilter.setOnClickListener(this);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                LogUtils.LogShow("position = " + position);
                for (int i = 0; i < adapter.getAllData().size(); i++) {
                    CheckDataInfo checkDataInfo = adapter.getAllData().get(i);
                    checkDataInfo.setChecked(false);
                    if (position == i) {
                        checkDataInfo.setChecked(true);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getHttpEditInfo() {
        OkGo.<String>post(MyApplication.getInstance().getBsaeUrl() + HttpUrlUtils.check_to_edit_url)
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
                        LogUtils.LogShow("result = " + result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            JSONArray goodsArray = new JSONArray(jsonObject.getString("goodsList"));
                            goodsList.clear();
                            for (int i = 0; i < goodsArray.length(); i++) {
                                goodsList.add(goodsArray.getString(i));
                            }

                            Gson gson = new Gson();
                            JSONArray carTypeArray = new JSONArray(jsonObject.getString("carTypeList"));
                            carTypeBeanList.clear();
                            for (int i = 0; i < carTypeArray.length(); i++) {
                                CheckDataInfo.CarTypeBean carTypeBean = gson.fromJson(carTypeArray.getString(i), CheckDataInfo.CarTypeBean.class);
                                carTypeBeanList.add(carTypeBean);
                            }

                            lane = jsonObject.getString("lane");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.showShortToast(MyApplication.getInstance(), "连接服务器异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }


    @Override
    public void onClick(View view) {
        int index = getCheckedPosition();
        switch (view.getId()) {
            case R.id.btn_initial_survey:
                /*String[] strs = MyApplication.getInstance().getPowerStr("首页").split(",");
                if (strs.length != 4)
                    return;
                if (strs[0].equals("0")) {
                    ToastUtils.showShortToast(MyApplication.getInstance(), "当前登录的账户没有该权限，请与管理员联系");
                    return;
                }*/

                showDialog(1, index);
                break;
            case R.id.btn_recheck:
                /*String[] str1s = MyApplication.getInstance().getPowerStr("首页").split(",");
                if (str1s.length != 4)
                    return;
                if (str1s[2].equals("0")) {
                    ToastUtils.showShortToast(MyApplication.getInstance(), "当前登录的账户没有该权限，请与管理员联系");
                    return;
                }*/

                if (0 == adapter.getAllData().size()) {
                    ToastUtils.showShortToast(getActivity(), "暂无数据");
                    return;
                }
                if (-1 == index) {
                    ToastUtils.showShortToast(getActivity(), "请选择一项");
                    return;
                }
                showDialog(2, index);
                break;
            case R.id.btn_print:
                if (0 == adapter.getAllData().size()) {
                    ToastUtils.showShortToast(getActivity(), "暂无数据");
                    return;
                }
                if (-1 == index) {
                    ToastUtils.showShortToast(getActivity(), "请选择一项");
                    return;
                }
                httpPrint(index);
                break;
            case R.id.btn_pass:

                break;
            case R.id.iv_filter:
                showFilterPopup();
                break;
        }
    }

    private void httpPrint(int index) {
        OkGo.<String>post(MyApplication.getInstance().getBsaeUrl() + HttpUrlUtils.check_print_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("id", adapter.getItem(index).getId())
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        ProgressDialogUtils.showLoading(getActivity());
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body();
                        LogUtils.LogShow(result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            if (!TextUtils.isEmpty(jsonObject.getString("data"))) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ProgressDialogUtils.dismissLoading();
                                        ToastUtils.showShortToast(MyApplication.getInstance(), "打印成功");
                                    }
                                }, 3000);
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
                        ToastUtils.showShortToast(MyApplication.getInstance(), "连接服务器异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });


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

    public void showItemDialog(final EditText etNum, final TextView tv, final TextView tv_limit_weight) {
        final List<CheckDataInfo.CarTypeBean> carList = new ArrayList<>();
        if (!TextUtils.isEmpty(etNum.getText().toString())) {
            for (int i = 0; i < carTypeBeanList.size(); i++) {
                if (etNum.getText().toString().equals(carTypeBeanList.get(i).getAxisNum())) {
                    carList.add(carTypeBeanList.get(i));
                }
            }
        } else {
            carList.addAll(carTypeBeanList);
        }
        final String[] carItems = new String[carList.size()];
        for (int i = 0; i < carList.size(); i++) {
            carItems[i] = carList.get(i).getName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  //先得到构造器
        //builder.setTitle("请选择"); //设置标题
        builder.setItems(carItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                carTypeId = which;
                dialog.dismiss();
                etNum.setText(carList.get(which).getAxisNum());
                tv.setText(carItems[which]);
                tv_limit_weight.setText(String.valueOf(carList.get(which).getCheckLimit()));
            }
        });
        builder.create().show();
    }

    public void showItemDialog(final String[] items, final EditText et) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  //先得到构造器
        //builder.setTitle("请选择"); //设置标题
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                et.setText(items[which]);
            }
        });
        builder.create().show();
    }

    private int getCheckedPosition() {
        int index = -1;
        for (int i = 0; i < adapter.getAllData().size(); i++) {
            if (adapter.getAllData().get(i).isChecked())
                index = i;
        }
        return index;
    }

    private CheckDataInfo editCheckDataInfo;
    private EditText et_total_weight;
    private TextView tv_limit_weight;
    private EditText et_over_weight;
    AlertDialog editDialog;

    private void showDialog(final int type, final int index) {
        if (editDialog != null)
            editDialog = null;
        editDialog = new AlertDialog.Builder(getActivity()).create();
        editDialog.show();
        editDialog.setCanceledOnTouchOutside(false);
        Window window = editDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(R.layout.dialog_weight_info_layout);

        ImageView iv_close = (ImageView) window.findViewById(R.id.iv_close);
        final EditText et_axles_num = (EditText) window.findViewById(R.id.et_axles_num);
        final TextView tv_axle_type = (TextView) window.findViewById(R.id.tv_axle_type);
        final ImageView iv_axle_type = (ImageView) window.findViewById(R.id.iv_axle_type);
        et_total_weight = (EditText) window.findViewById(R.id.et_total_weight);
        tv_limit_weight = (TextView) window.findViewById(R.id.tv_limit_weight);
        et_over_weight = (EditText) window.findViewById(R.id.et_over_weight);
        final EditText et_goods_class = (EditText) window.findViewById(R.id.et_goods_class);
        final ImageView iv_goods_class = (ImageView) window.findViewById(R.id.iv_goods_class);
        final EditText et_license_plate = (EditText) window.findViewById(R.id.et_license_plate);
        final EditText et_driver = (EditText) window.findViewById(R.id.et_driver);
        Button btn_clear = (Button) window.findViewById(R.id.btn_clear);
        Button btn_initial_survey = (Button) window.findViewById(R.id.btn_initial_survey);
        Button btn_pass = (Button) window.findViewById(R.id.btn_pass);

        MyApplication.getInstance().getMyService().carFlag = true;
        if (type == 1) {
            btn_initial_survey.setText("初检");
            btn_initial_survey.setBackgroundResource(R.drawable.shape_check_data_button_01);
        } else {
            btn_initial_survey.setText("复检");
            btn_initial_survey.setBackgroundResource(R.drawable.shape_check_data_button_02);
            btn_clear.setVisibility(View.GONE);

            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, getResources().getDisplayMetrics());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            btn_initial_survey.setLayoutParams(layoutParams);

            CheckDataInfo checkDataInfo = adapter.getItem(index);
            et_axles_num.setText(checkDataInfo.getCarType().getAxisNum());
            tv_axle_type.setText(checkDataInfo.getCarType().getName());
            et_total_weight.setText(String.valueOf(checkDataInfo.getAmt()));
            tv_limit_weight.setText(String.valueOf(checkDataInfo.getCarType().getCheckLimit()));
            et_over_weight.setText(String.valueOf(checkDataInfo.getOverAmt()));
            et_goods_class.setText(checkDataInfo.getGoods());
            et_license_plate.setText(checkDataInfo.getCarNo());
            et_driver.setText(checkDataInfo.getDriver());
            et_axles_num.setEnabled(false);
            tv_axle_type.setEnabled(false);
            iv_axle_type.setEnabled(false);
            et_total_weight.setEnabled(false);
            et_over_weight.setEnabled(false);
            et_goods_class.setEnabled(false);
            iv_goods_class.setEnabled(false);
            et_license_plate.setEnabled(false);
            et_driver.setEnabled(false);
            btn_clear.setEnabled(false);
        }
        tv_axle_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showItemDialog(et_axles_num, tv_axle_type, tv_limit_weight);
            }
        });
        iv_axle_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showItemDialog(et_axles_num, tv_axle_type, tv_limit_weight);
            }
        });
        iv_goods_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] goodsItems = new String[goodsList.size()];
                for (int i = 0; i < goodsList.size(); i++) {
                    goodsItems[i] = goodsList.get(i);
                }
                showItemDialog(goodsItems, et_goods_class);
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carTypeId = -1;
                et_axles_num.setText("");
                tv_axle_type.setText("");
                et_total_weight.setText("");
                tv_limit_weight.setText("");
                et_over_weight.setText("");
                et_goods_class.setText("");
                et_license_plate.setText("");
                et_driver.setText("");
            }
        });
        btn_initial_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 1) {
                    if (carTypeId == -1) {
                        ToastUtils.showShortToast(MyApplication.getInstance(), "请选择车轴类型");
                        return;
                    }
                    if (TextUtils.isEmpty(et_total_weight.getText().toString())) {
                        ToastUtils.showShortToast(MyApplication.getInstance(), "请输入车货总量");
                        return;
                    }
                    if (TextUtils.isEmpty(et_over_weight.getText().toString())) {
                        ToastUtils.showShortToast(MyApplication.getInstance(), "请输入总量超重");
                        return;
                    }
                    if (TextUtils.isEmpty(et_axles_num.getText().toString())) {
                        ToastUtils.showShortToast(MyApplication.getInstance(), "请输入轴数");
                        return;
                    } else {
                        if (!et_axles_num.getText().toString().equals(tv_axle_type.getText().toString().substring(0, 1))) {
                            ToastUtils.showShortToast(MyApplication.getInstance(), "请输入正确的轴数");
                            return;
                        }
                    }
                    if (TextUtils.isEmpty(et_goods_class.getText().toString())) {
                        ToastUtils.showShortToast(MyApplication.getInstance(), "请输入或选择货物类别");
                        return;
                    }
                    if (TextUtils.isEmpty(et_license_plate.getText().toString())) {
                        ToastUtils.showShortToast(MyApplication.getInstance(), "请输入车牌号码");
                        return;
                    }
                    if (TextUtils.isEmpty(et_driver.getText().toString())) {
                        ToastUtils.showShortToast(MyApplication.getInstance(), "请输入驾驶员");
                        return;
                    }
                    editCheckDataInfo = new CheckDataInfo();
                    editCheckDataInfo.setCarNo(et_license_plate.getText().toString());
                    editCheckDataInfo.setAmt(Double.parseDouble(TextUtils.isEmpty(et_total_weight.getText().toString()) ? "0" : et_total_weight.getText().toString()));
                    editCheckDataInfo.setOverAmt(Double.parseDouble(TextUtils.isEmpty(et_over_weight.getText().toString()) ? "0" : et_over_weight.getText().toString()));
                    editCheckDataInfo.setGoods(et_goods_class.getText().toString());
                    editCheckDataInfo.setDriver(et_driver.getText().toString());
                    editCheckDataInfo.setLane(lane);
                    httpInitialCheckAdd();
                } else {
                    if (TextUtils.isEmpty(et_total_weight.getText().toString())) {
                        ToastUtils.showLongToast(getActivity(), "磅信息未能获取，请检查车是否上磅！");
                        return;
                    }
                    httpReCheck(index);
                }
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDialog();
            }
        });
        et_over_weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (!TextUtils.isEmpty(et_total_weight.getText().toString()) && !TextUtils.isEmpty(tv_limit_weight.getText().toString())) {
                        BigDecimal dataA = new BigDecimal(et_total_weight.getText().toString());
                        BigDecimal dataB = new BigDecimal(tv_limit_weight.getText().toString());
                        //大于为1，相同为0，小于为-1
                        if (dataA.compareTo(dataB) == 1) {
                            et_over_weight.setText("" + sub(Double.valueOf(et_total_weight.getText().toString()), Double.valueOf(tv_limit_weight.getText().toString())));
                        } else {
                            et_over_weight.setText("0");
                        }
                    }
                }
            }
        });
        editDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                System.out.println("dialog关闭");
                MyApplication.getInstance().getMyService().carFlag = false;
            }
        });

    }

    private void closeDialog() {
        if (editDialog != null) {
            editDialog.dismiss();
            editDialog = null;
        }
    }

    private void httpGetCarInfo() {
        OkGo.<String>post(MyApplication.getInstance().getBsaeUrl() + HttpUrlUtils.get_scan_data_url)
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
                            carAmt = jsonObject.getString("carAmt");
                            LogUtils.LogShow(" carAmt = " + carAmt);
                            if (et_total_weight != null && tv_limit_weight != null && et_over_weight != null) {
                                et_total_weight.setText(carAmt);
                                et_total_weight.setSelection(carAmt.length());
                                if (!TextUtils.isEmpty(et_total_weight.getText().toString()) && !TextUtils.isEmpty(tv_limit_weight.getText().toString())) {
                                    BigDecimal dataA = new BigDecimal(et_total_weight.getText().toString());
                                    BigDecimal dataB = new BigDecimal(tv_limit_weight.getText().toString());
                                    //大于为1，相同为0，小于为-1
                                    if (dataA.compareTo(dataB) == 1) {
                                        et_over_weight.setText("" + sub(Double.valueOf(et_total_weight.getText().toString()), Double.valueOf(tv_limit_weight.getText().toString())));
                                    } else {
                                        et_over_weight.setText("0");
                                    }
                                    et_over_weight.setSelection(et_over_weight.getText().toString().length());
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
                    }
                });
    }

    private void httpReCheck(final int index) {
        String carWeight = carAmt;
        final CheckDataInfo checkDataInfo = adapter.getItem(index);
        if (TextUtils.isEmpty(carWeight))
            carWeight = "0";
        BigDecimal dataA = new BigDecimal("" + checkDataInfo.getAmt());
        BigDecimal dataB = new BigDecimal(carWeight);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        double checkUnWeight = 0;
        if (dataA.compareTo(dataB) == 1) {
            checkUnWeight = sub(checkDataInfo.getAmt(), Double.valueOf(carWeight));
            builder.setMessage("请确认正在为" + checkDataInfo.getCarNo() + "进行复核，复核显示原载重量为" + checkDataInfo.getAmt() + "吨，现载重为" + carWeight + "吨，卸货" + sub(checkDataInfo.getAmt(), Double.valueOf(carWeight)) + "吨。信息正确请点击确认按钮！");
        } else {
            checkUnWeight = 0;
            builder.setMessage("请确认正在为" + checkDataInfo.getCarNo() + "进行复核，复核显示原载重量为" + checkDataInfo.getAmt() + "吨，现载重为" + carWeight + "吨，卸货0吨。信息正确请点击确认按钮！");
        }
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final double checkUn = checkUnWeight;
        final String finalCarWeight = carWeight;
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                OkGo.<String>post(MyApplication.getInstance().getBsaeUrl() + HttpUrlUtils.re_check_save_url)
                        .tag(this)
                        .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                        .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                        .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                        .params("id", checkDataInfo.getId())
                        .params("checkAmt", finalCarWeight)
                        .params("checkUnLoad", checkUn)
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
                                    if (!TextUtils.isEmpty(jsonObject.getString("data"))) {
                                        closeDialog();
                                        ToastUtils.showShortToast(getActivity(), jsonObject.getString("data"));
                                        swipeRefreshLayout.setRefreshing(true);
                                        onRefresh();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    try {
                                        if (!TextUtils.isEmpty(jsonObject.getString("error")))
                                            ToastUtils.showShortToast(getActivity(), jsonObject.getString("error"));
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onError(Response<String> response) {
                                super.onError(response);
                                ToastUtils.showShortToast(MyApplication.getInstance(), "连接服务器异常");
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                ProgressDialogUtils.dismissLoading();
                            }
                        });
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private void httpInitialCheckAdd() {
        OkGo.<String>post(MyApplication.getInstance().getBsaeUrl() + HttpUrlUtils.initial_check_save_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("carType.id", carTypeBeanList.get(carTypeId).getId())
                .params("carNo", editCheckDataInfo.getCarNo())
                .params("amt", editCheckDataInfo.getAmt())
                .params("overAmt", editCheckDataInfo.getOverAmt())
                .params("goods", editCheckDataInfo.getGoods())
                .params("lane", lane)
                .params("driver", editCheckDataInfo.getDriver())
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        ProgressDialogUtils.showLoading(getActivity());
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = response.body();
                        LogUtils.LogShow(result);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            if (jsonObject.getString("data").indexOf("初检成功") != -1) {
                                closeDialog();
                                swipeRefreshLayout.setRefreshing(true);
                                onRefresh();
                                ToastUtils.showShortToast(MyApplication.getInstance(), "初检成功");
                                getHttpEditInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtils.showShortToast(MyApplication.getInstance(), "初检失败");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.showShortToast(MyApplication.getInstance(), "连接服务器异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressDialogUtils.dismissLoading();
                    }
                });

    }


    private void showFilterPopup() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(R.layout.dialog_check_filter_layout);

        final EditText et_carNO = (EditText) window.findViewById(R.id.et_carNO);
        ImageView iv_carNo = (ImageView) window.findViewById(R.id.iv_carNo);
        final TextView tv_begin_time = (TextView) window.findViewById(R.id.tv_begin_time);
        ImageView iv_begin_time = (ImageView) window.findViewById(R.id.iv_begin_time);
        final TextView tv_end_time = (TextView) window.findViewById(R.id.tv_end_time);
        ImageView iv_end_time = (ImageView) window.findViewById(R.id.iv_end_time);
        final RadioButton rb_yes = (RadioButton) window.findViewById(R.id.rb_yes);
        final RadioButton rb_no = (RadioButton) window.findViewById(R.id.rb_no);
        Button btn_clear = (Button) window.findViewById(R.id.btn_clear);
        Button btn_cancle = (Button) window.findViewById(R.id.btn_cancle);
        Button btn_ok = (Button) window.findViewById(R.id.btn_ok);
        et_carNO.setText(carNo);
        tv_begin_time.setText(beginTime);
        tv_end_time.setText(endTime);
        if (isCheck)
            rb_yes.setChecked(true);
        else
            rb_no.setChecked(true);
        tv_begin_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar beginDate = Calendar.getInstance();
                String beginStr = tv_begin_time.getText().toString();
                if(!TextUtils.isEmpty(beginStr))
                    beginDate.set(Integer.parseInt(beginStr.substring(0,4)),Integer.parseInt(beginStr.substring(5,7))-1,Integer.parseInt(beginStr.substring(8,10))
                            ,Integer.parseInt(beginStr.substring(11,13)),Integer.parseInt(beginStr.substring(14,16)));
                showTimePickerDialog(tv_begin_time,beginDate);
            }
        });
        iv_begin_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar beginDate = Calendar.getInstance();
                String beginStr = tv_begin_time.getText().toString();
                if(!TextUtils.isEmpty(beginStr))
                    beginDate.set(Integer.parseInt(beginStr.substring(0,4)),Integer.parseInt(beginStr.substring(5,7))-1,Integer.parseInt(beginStr.substring(8,10))
                            ,Integer.parseInt(beginStr.substring(11,13)),Integer.parseInt(beginStr.substring(14,16)));
                showTimePickerDialog(tv_begin_time,beginDate);
            }
        });
        tv_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar endDate = Calendar.getInstance();
                String endStr = tv_end_time.getText().toString();
                if(!TextUtils.isEmpty(endStr))
                    endDate.set(Integer.parseInt(endStr.substring(0,4)),Integer.parseInt(endStr.substring(5,7))-1,Integer.parseInt(endStr.substring(8,10))
                            ,Integer.parseInt(endStr.substring(11,13)),Integer.parseInt(endStr.substring(14,16)));
                showTimePickerDialog(tv_end_time,endDate);
            }
        });
        iv_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar endDate = Calendar.getInstance();
                String endStr = tv_end_time.getText().toString();
                if(!TextUtils.isEmpty(endStr))
                    endDate.set(Integer.parseInt(endStr.substring(0,4)),Integer.parseInt(endStr.substring(5,7))-1,Integer.parseInt(endStr.substring(8,10))
                            ,Integer.parseInt(endStr.substring(11,13)),Integer.parseInt(endStr.substring(14,16)));
                showTimePickerDialog(tv_end_time,endDate);
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_carNO.setText("");
                tv_begin_time.setText("");
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
                carNo = et_carNO.getText().toString();
                beginTime = tv_begin_time.getText().toString();
                endTime = tv_end_time.getText().toString();
                if (rb_yes.isChecked())
                    isCheck = true;
                else
                    isCheck = false;
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
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                tv.setText(format.format(date));
            }
        }).setType(new boolean[]{true, true, true, true, true, false})
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    //两个double类型相减
    public static Double sub(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.subtract(b2).doubleValue());
    }


}
