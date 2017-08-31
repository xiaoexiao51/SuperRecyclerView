package com.superrecyclerview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.superrecyclerview.interfaces.IRefreshHeader;

/**
 * Created by MMM on 2017/8/7.
 */

public class SuperHeader extends LinearLayout implements IRefreshHeader {

    public SuperHeader(Context context) {
        this(context, null);
    }

    public SuperHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {

    }

    @Override
    public void onReset() {

    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onRefreshing() {

    }

    @Override
    public void onMove(float offSet, float sumOffSet) {

    }

    @Override
    public boolean onRelease() {
        return false;
    }

    @Override
    public void onComplete() {

    }

    @Override
    public View getHeaderView() {
        return null;
    }

    @Override
    public int getVisibleHeight() {
        return 0;
    }
}
