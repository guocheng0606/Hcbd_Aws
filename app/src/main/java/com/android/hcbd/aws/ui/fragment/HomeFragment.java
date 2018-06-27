package com.android.hcbd.aws.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.hcbd.aws.MyApplication;
import com.android.hcbd.aws.R;
import com.android.hcbd.aws.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 首页
 */
public class HomeFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.tv_pre)
    TextView tvPre;
    @BindView(R.id.tv_accurate)
    TextView tvAccurate;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private PreCheckFragment preCheckFragment;
    private AccurateCheckFragment accurateCheckFragment;

    Unbinder unbinder;

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        tvPre.setSelected(false);
        tvAccurate.setSelected(true);
        fragmentManager = getChildFragmentManager();
        setTabSelection(0);
        initListener();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initListener() {
        tvPre.setOnClickListener(this);
        tvAccurate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_accurate:
                tvPre.setSelected(false);
                tvAccurate.setSelected(true);
                setTabSelection(0);
                break;
            case R.id.tv_pre:
                tvPre.setSelected(true);
                tvAccurate.setSelected(false);
                setTabSelection(1);
                break;
        }
    }

    private void setTabSelection(int i) {
        fragmentTransaction = fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        switch (i) {
            case 0:
                if(accurateCheckFragment == null){
                    accurateCheckFragment = new AccurateCheckFragment();
                    fragmentTransaction.add(R.id.frameLayout,accurateCheckFragment);
                }else{
                    fragmentTransaction.show(accurateCheckFragment);
                }
                break;
            case 1:
                if(preCheckFragment == null){
                    preCheckFragment = new PreCheckFragment();
                    fragmentTransaction.add(R.id.frameLayout,preCheckFragment);
                }else{
                    fragmentTransaction.show(preCheckFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if(preCheckFragment != null) fragmentTransaction.hide(preCheckFragment);
        if(accurateCheckFragment != null) fragmentTransaction.hide(accurateCheckFragment);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser){
            //不可见
            System.out.println("as = Home不可见");
            MyApplication.getInstance().getMyService().preFlag = false;
            MyApplication.getInstance().getMyService().carFlag = false;
        }else{
            //可见
            System.out.println("as = Home可见");
            MessageEvent messageEvent = new MessageEvent();
            messageEvent.setEventId(MessageEvent.EVENT_DATA_THREAD);
            EventBus.getDefault().post(messageEvent);
        }

    }
}
