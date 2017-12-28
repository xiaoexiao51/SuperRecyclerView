package com.superrecyclerview.base;

import android.os.Bundle;

import com.superrecyclerview.swipbackhelper.SwipeBackHelper;
import com.superrecyclerview.swipbackhelper.SwipeListener;

/**
 * Created by MMM on 2017/8/8.
 * 滑动退出Activity，参考：https://github.com/Jude95/SwipeBackHelper
 */

public abstract class BaseSwipeBackActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
                .setSwipeEdgePercent(0.2f)//可滑动的范围,0.5表示为左边50%的屏幕
                .setSwipeSensitivity(0.3f)//对横向滑动的敏感程度,0为迟钝 1为敏感
                .setClosePercent(0.5f)//触发关闭百分比
                .setScrimColor(0xFFFFCD03)//底层阴影颜色
                .setDisallowInterceptTouchEvent(false)//抢占事件
                .addListener(new SwipeListener() {//滑动监听

                    @Override
                    public void onScroll(float percent, int px) {//滑动的百分比与距离
                    }

                    @Override
                    public void onEdgeTouch() {//当开始滑动
                    }

                    @Override
                    public void onScrollToClose() {//当滑动关闭
                    }
                });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }
}
