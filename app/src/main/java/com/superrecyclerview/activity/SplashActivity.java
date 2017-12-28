package com.superrecyclerview.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.superrecyclerview.R;
import com.superrecyclerview.base.BaseActivity;
import com.superrecyclerview.utils.ActivityUtils;
import com.superrecyclerview.utils.AnimHelper;
import com.superrecyclerview.utils.CommonUtils;

import butterknife.Bind;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by MMM on 2017/8/8.
 * SplashActivity
 */
public class SplashActivity extends BaseActivity {

    @Bind(R.id.iv_splash)
    ImageView mIvSplash;
    @Bind(R.id.tv_splash)
    TextView mTvSplash;

    @Override
    protected int getViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        // 设置高斯模糊
        Glide.with(this)
                .load(R.drawable.ic_splash)
                .bitmapTransform(new BlurTransformation(this, 25, 5))
                .into(mIvSplash);

        // 系统提供倒计时
        new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTvSplash.setText(millisUntilFinished / 1000 + "");
                AnimHelper.doHeartBeat(mTvSplash, 800);
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        CommonUtils.getHandler().postDelayed(spRunnable, 3000);
    }

    private Runnable spRunnable = new Runnable() {
        @Override
        public void run() {
            ActivityUtils.launchActivity(SplashActivity.this, MainActivity.class);
            finish();
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        CommonUtils.getHandler().removeCallbacks(spRunnable);
    }

    public static final int LOCATION_REQUEST_CODE = 300;

    // 申请定位权限
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            } else {
                // 权限申请
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == LOCATION_REQUEST_CODE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                MessageUtils.showInfo(this, "获取位置权限成功");
//            } else {
//                MessageUtils.showQuestion(SplashActivity.this, "提示", "获取位置信息，需要开启定位权限！" +
//                                "\n请到设置-应用信息-权限管理中开启。",
//                        new MessageDialog.OnChooseResultListener() {
//                            @Override
//                            public void onChooseResult(boolean flag) {
//                                if (flag) {
//                                    Intent intent = new Intent(
//                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                    intent.setData(Uri.parse("package:" + getPackageName()));
//                                    startActivity(intent);
//                                }
//                            }
//                        });
//            }
//        }
//    }
}
