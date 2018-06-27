package com.android.hcbd.aws.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.android.hcbd.aws.MyApplication;
import com.android.hcbd.aws.R;
import com.android.hcbd.aws.util.HttpUrlUtils;
import com.android.hcbd.aws.util.LogUtils;
import com.android.hcbd.aws.util.ProgressDialogUtils;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.code.X;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.series.Series;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.tikeyc.tandroidechartlibrary.TEChartWebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarNoticeChartsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.btn_time)
    Button btnTime;
    @BindView(R.id.btn_day)
    Button btnDay;
    @BindView(R.id.btn_week)
    Button btnWeek;
    @BindView(R.id.btn_month)
    Button btnMonth;
    @BindView(R.id.barChartWebView)
    TEChartWebView barChartWebView;

    private String startTime = "";
    private String endTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_car_notice_charts);
        ButterKnife.bind(this);

        startTime = getIntent().getStringExtra("start_time");
        endTime = getIntent().getStringExtra("end_time");

        btnTime.setSelected(true);
        btnDay.setSelected(false);
        btnWeek.setSelected(false);
        btnMonth.setSelected(false);
        initData();
        ivBack.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnDay.setOnClickListener(this);
        btnWeek.setOnClickListener(this);
        btnMonth.setOnClickListener(this);
    }

    private void initData() {
        OkGo.<String>post(MyApplication.getInstance().getBsaeUrl() + HttpUrlUtils.led_car_notice_url)
                .tag(this)
                .params("sessionOper.code", MyApplication.getInstance().getLoginInfo().getUserInfo().getCode())
                .params("sessionOper.orgCode", MyApplication.getInstance().getLoginInfo().getUserInfo().getOrgCode())
                .params("token", MyApplication.getInstance().getLoginInfo().getToken())
                .params("beginTime", startTime)
                .params("endTime", endTime)
                .params("timeType", btnDay.isSelected()?"day":(btnWeek.isSelected()?"week":(btnMonth.isSelected()?"month":"")))
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        ProgressDialogUtils.showLoading(CarNoticeChartsActivity.this);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        final String result = response.body();
                        LogUtils.LogShow(result);
                        //设置数据源
                        barChartWebView.setDataSource(new TEChartWebView.DataSource() {
                            @Override
                            public GsonOption markLineChartOptions() {
                                return getLineChartOption(result);
                            }
                        });
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressDialogUtils.dismissLoading();
                    }
                });
    }

    public GsonOption getLineChartOption(String s) {
        GsonOption option = new GsonOption();
        try {
            JSONObject jsonObject = new JSONObject(s);
            Object[] names = jsonObject.getString("nameList").split(",");
            Object[] times = jsonObject.getString("timeList").split(",");
            Object[] sums = jsonObject.getString("sumList").split(",");
            option.legend(names);
            option.toolbox().show(true).feature(/*Tool.mark,*/ Tool.dataView, new MagicType(Magic.line, Magic.bar), Tool.restore/*,Tool.saveAsImage*/);
            option.calculable(true);
            option.tooltip().trigger(Trigger.axis);

            CategoryAxis categoryAxis = new CategoryAxis();
            categoryAxis.axisLine().onZero(false);
            categoryAxis.boundaryGap(false);
            categoryAxis.data(times);
            option.xAxis(categoryAxis);

            ValueAxis valueAxis = new ValueAxis();
            valueAxis.position(X.right);
            option.yAxis(valueAxis);

            /*DataZoom dataZoom = new DataZoom();
            dataZoom.setType(DataZoomType.slider);
            dataZoom.setStart(0);
            dataZoom.setEnd(16);
            option.dataZoom(dataZoom);*/

            List<Series> series = new ArrayList<>();
            List<PieData> pieDatas = new ArrayList<>();
            for (int i = 0; i < names.length; i++) {
                Object[] datas = jsonObject.getString("dataList" + i).split(",");
                if(i==0){
                    Line line = new Line();
                    line.smooth(true).name(String.valueOf(names[i])).data(datas);
                    series.add(line);
                }else{
                    Bar bar = new Bar();
                    bar.name(String.valueOf(names[i])).tooltip().trigger(Trigger.item);
                    bar.stack("合计").data(datas);
                    series.add(bar);
                }
                PieData pieData = new PieData((String) names[i],sums[i]);
                pieDatas.add(pieData);
            }

            Pie pie = new Pie();
            pie.name("进站分析").tooltip().trigger(Trigger.item).formatter("{a} <br/>{b} : {c}辆({d}%)");
            Object[] cen = {160,130};
            pie.center(cen).radius(50).itemStyle().normal().labelLine().length(20);
            pie.setData(pieDatas);
            series.add(pie);

            option.setSeries(series);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return option;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_time:
                btnTime.setSelected(true);
                btnDay.setSelected(false);
                btnWeek.setSelected(false);
                btnMonth.setSelected(false);
                initData();
                break;
            case R.id.btn_day:
                btnTime.setSelected(false);
                btnDay.setSelected(true);
                btnWeek.setSelected(false);
                btnMonth.setSelected(false);
                initData();
                break;
            case R.id.btn_week:
                btnTime.setSelected(false);
                btnDay.setSelected(false);
                btnWeek.setSelected(true);
                btnMonth.setSelected(false);
                initData();
                break;
            case R.id.btn_month:
                btnTime.setSelected(false);
                btnDay.setSelected(false);
                btnWeek.setSelected(false);
                btnMonth.setSelected(true);
                initData();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //根据 Tag 取消请求
        OkGo.getInstance().cancelTag(this);
    }

}
