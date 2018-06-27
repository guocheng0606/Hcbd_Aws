package com.android.hcbd.aws.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.android.hcbd.aws.MyApplication;
import com.android.hcbd.aws.R;
import com.android.hcbd.aws.entity.CarTypeInfo;
import com.android.hcbd.aws.event.MessageEvent;
import com.android.hcbd.aws.util.HttpUrlUtils;
import com.android.hcbd.aws.util.LogUtils;
import com.android.hcbd.aws.util.ToastUtils;
import com.android.hcbd.aws.viewholder.CarTypeViewHolder;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 车型类型
 */
public class CarTypeListActivity extends BaseActivity implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private int currentPage = 1;
    private RecyclerArrayAdapter<CarTypeInfo> adapter;
    private List<CarTypeInfo> carTypeInfoList = new ArrayList<>();

    private CarTypeInfo searchInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_type_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        initHttpData();
        initListener();
    }

    private void initListener() {
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(CarTypeListActivity.this,CarTypeEditActivity.class);
                intent.putExtra("data",adapter.getAllData().get(position));
                startActivity(intent);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getEventId()){
            case MessageEvent.EVENT_CARTYPE_SEARCH:
                searchInfo = (CarTypeInfo) event.getObj();
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
                break;
            case MessageEvent.EVENT_CARTYPE_EDIT_SUCCESS:
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        OkGo.getInstance().cancelTag(this);
    }

    private void initHttpData() {
        OkGo.<String>post(MyApplication.getInstance().getBsaeUrl()+HttpUrlUtils.car_type_list_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("page.currentPage", currentPage)
                .params("name", searchInfo == null ? "":searchInfo.getName())
                .params("axisNum", searchInfo == null ? "":searchInfo.getAxisNum())
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
                            if(jsonArray.length() > 0){
                                carTypeInfoList.clear();
                                Gson gson = new Gson();
                                for(int i=0;i<jsonArray.length();i++){
                                    CarTypeInfo carTypeInfo = gson.fromJson(jsonArray.getString(i),CarTypeInfo.class);
                                    carTypeInfoList.add(carTypeInfo);
                                }
                                if(currentPage == 1)
                                    adapter.clear();
                                adapter.addAll(carTypeInfoList);
                            }else{
                                if(currentPage == 1){
                                    adapter.clear();
                                }else{
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
                        if(currentPage == 1){
                            ToastUtils.showShortToast(MyApplication.getInstance(),"连接服务器异常");
                        }else{
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerDecoration itemDecoration = new DividerDecoration(0xFFEDEDED, 1, 0, 0);
        itemDecoration.setDrawLastItem(true);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapterWithProgress(adapter = new RecyclerArrayAdapter<CarTypeInfo>(this) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new CarTypeViewHolder(parent);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
                break;
            case R.id.action_search:
                Intent intent = new Intent(this,CarTypeSearchActivity.class);
                intent.putExtra("searchInfo",searchInfo);
                startActivity(intent);
                break;
            case R.id.action_edit:
                String[] strs = MyApplication.getInstance().getPowerStr("车型信息").split(",");
                if(strs.length == 4){
                    if(strs[0].equals("1")){
                        startActivity(new Intent(this,CarTypeEditActivity.class));
                    }else {
                        ToastUtils.showShortToast(MyApplication.getInstance(), "您没有车型信息录入权限,请与管理员联系！");
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_car_type, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        initHttpData();
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        initHttpData();
    }

}
