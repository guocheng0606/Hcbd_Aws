package com.android.hcbd.aws.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import com.android.hcbd.aws.R;

/**
 * Created by guocheng on 2017/7/17.
 */

public class FullDialog extends AlertDialog{

    private Context mContext;

    public FullDialog(Context context) {
        super(context, R.style.FullDialog); // 自定义全屏style
        this.mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity= Gravity.BOTTOM;
        layoutParams.width= LayoutParams.MATCH_PARENT;
        layoutParams.height= LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }
}
