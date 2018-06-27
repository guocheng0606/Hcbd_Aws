package com.android.hcbd.aws.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.android.hcbd.aws.R;
import com.android.hcbd.aws.adapter.FragmentAdapter;
import com.android.hcbd.aws.crash.Cockroach;
import com.android.hcbd.aws.event.MessageEvent;
import com.android.hcbd.aws.ui.fragment.HomeFragment;
import com.android.hcbd.aws.ui.fragment.InspectionHistoryFragment;
import com.android.hcbd.aws.ui.fragment.PreHistoryFragment;
import com.android.hcbd.aws.ui.fragment.SettingFragment;
import com.android.hcbd.aws.util.ToastUtils;
import com.android.hcbd.aws.widget.AlphaIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity{

    private static final String TAG = "AWS";
    private static final String SUB = "[MainActivity]#";

    @BindView(R.id.alphaIndicator)
    AlphaIndicator alphaIndicator;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        fragments.add(HomeFragment.newInstance(null,null));
        fragments.add(PreHistoryFragment.newInstance(null,null));
        fragments.add(InspectionHistoryFragment.newInstance(null,null));
        fragments.add(SettingFragment.newInstance(null,null));

        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),fragments));
        viewPager.setOffscreenPageLimit(3);
        alphaIndicator.setViewPager(viewPager);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getEventId()){
            case MessageEvent.EVENT_LOGINOUT:
                startActivity(new Intent(this,LoginActivity.class));
                finishActivity();
                break;
        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private static boolean mBackKeyPressed = false;//记录是否有首次按键
    @Override
    public void onBackPressed() {
        if(!mBackKeyPressed){
            ToastUtils.showShortToast(MainActivity.this,"再按一次退出程序");
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录
                @Override
                public void run() {
                    mBackKeyPressed = false;
                }
            }, 2000);
        }else{//退出程序
            Cockroach.uninstall();
            this.finish();
            System.exit(0);
        }
    }

}
