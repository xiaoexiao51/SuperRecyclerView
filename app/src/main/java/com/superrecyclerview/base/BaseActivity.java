package com.superrecyclerview.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.superrecyclerview.R;
import com.superrecyclerview.SuperApplication;
import com.superrecyclerview.utils.CommonUtils;
import com.superrecyclerview.widget.StateLayout;

import butterknife.ButterKnife;

/**
 * Created by MMM on 2017/8/8.
 * 基类Activity
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    private StateLayout mStateLayout;
    private TextView mNotNetWork;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mContext = SuperApplication.applicationContext;
//        EventBus.getDefault().register(this);
//        ActivityHelper.getInstance().addActivity(this);
        mStateLayout = (StateLayout) findViewById(R.id.state_layout);
        mNotNetWork = (TextView) findViewById(R.id.tv_network);
        mStateLayout.addStatLayout(StateLayout.STATE_SUCCESS, getViewId());
        View mView = mStateLayout.getStateView(StateLayout.STATE_SUCCESS);
        ButterKnife.bind(this, mView);
//        initToolbar();

        if (!CommonUtils.checkNetwork(mContext)) {
            showErrorStateLayout();
        }

        init(mView);
        initData();
    }

    protected abstract int getViewId();

    protected abstract void init(View view);

    /**
     * 有网络请求的页面，重写该方法
     */
    protected void initData() {
    }

    protected void showLodingStateLayout() {
        mStateLayout.showStateView(StateLayout.STATE_LOADING);
    }

    protected void showSuccessStateLayout() {
        mStateLayout.showStateView(StateLayout.STATE_SUCCESS);
    }

    protected void showErrorStateLayout() {
        mStateLayout.showStateView(StateLayout.STATE_ERROR);
        mStateLayout.getStateView(StateLayout.STATE_ERROR)
                .findViewById(R.id.iv_error)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (CommonUtils.checkNetwork(mContext)) {
                            initData();
                            showLodingStateLayout();
                        } else {
//                            SystemUtils.startNetSetting(mContext);
                        }
                    }
                });
    }

    protected void showEmptyStateLayout() {
        mStateLayout.showStateView(StateLayout.STATE_EMPTY);
    }

    protected void showLoginStateLayout() {
        mStateLayout.showStateView(StateLayout.STATE_LOGIN);
        mStateLayout.getStateView(StateLayout.STATE_LOGIN)
                .findViewById(R.id.tv_login)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ActivityUtils.launchActivity(mContext, LoginActivity.class);
                    }
                });
    }

    /**
     * 初始化 Toolbar
     *
     * @param toolbar
     * @param homeAsUpEnabled
     * @param title
     */
    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    /**
     * 添加 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    /**
     * 添加 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // 设置tag，不然下面 findFragmentByTag(tag)找不到
        fragmentTransaction.add(containerViewId, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    /**
     * 替换 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void replaceFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * 替换 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            // 设置tag
            fragmentTransaction.replace(containerViewId, fragment, tag);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // 这里要设置tag，上面也要设置tag
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
        } else {
            // 存在则弹出在它上面的所有fragment，并显示对应fragment
            getSupportFragmentManager().popBackStack(tag, 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 断网提示
     */
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (!NetworkUtils.isConnected()) {
//            mNotNetWork.setVisibility(View.VISIBLE);
//        } else {
//            mNotNetWork.setVisibility(View.GONE);
//        }
//    }
}
