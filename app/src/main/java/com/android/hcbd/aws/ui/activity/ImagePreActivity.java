package com.android.hcbd.aws.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.hcbd.aws.MyApplication;
import com.android.hcbd.aws.R;
import com.android.hcbd.aws.entity.PreCheckDataInfo;
import com.android.hcbd.aws.entity.PreHistoryInfo;
import com.blankj.utilcode.util.BarUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagePreActivity extends AppCompatActivity {

    @BindView(R.id.pv_image)
    PhotoView pvImage;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.pb)
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pre);
        ButterKnife.bind(this);
        BarUtils.setColorNoTranslucent(this, 0xFF000000);

        String type = getIntent().getStringExtra("type");
        String imageUrl = "";
        String str = "";
        if(type.equals("1")){
            PreCheckDataInfo preCheckDataInfo = (PreCheckDataInfo) getIntent().getSerializableExtra("image_info");
            imageUrl = preCheckDataInfo.getImg();
            str = "时间：" + preCheckDataInfo.getCreateTime() + " \n车牌：" + preCheckDataInfo.getCarNo() + "    超限：" + preCheckDataInfo.getMinAmt() + "吨";
        }else{
            PreHistoryInfo preHistoryInfo = (PreHistoryInfo) getIntent().getSerializableExtra("image_info");
            imageUrl = preHistoryInfo.getImg();
            str = "时间：" + preHistoryInfo.getCreateTime() + " \n车牌：" + preHistoryInfo.getCarNo() + "    超限：" + preHistoryInfo.getMinAmt() + "吨";
        }

        Glide.with(this).load(MyApplication.getInstance().getBsaeUrl() + imageUrl).into(new GlideDrawableImageViewTarget(pvImage) {
            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                pb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                pvImage.setImageResource(R.drawable.default_image);
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                pvImage.setImageDrawable(resource);
                pb.setVisibility(View.GONE);
            }
        });
        tv.setText(str);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0,R.anim.activity_close);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,R.anim.activity_close);
    }

}
