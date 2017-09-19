package com.superrecyclerview.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.superrecyclerview.R;
import com.superrecyclerview.dialog.LoadingDialog;
import com.superrecyclerview.utils.ScreenUtils;
import com.superrecyclerview.utils.StringUtils;
import com.superrecyclerview.widget.SRStateLayout;

import butterknife.ButterKnife;

/**
 * Created by MMM on 2017/9/1.
 * BaseActivity基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    private SRStateLayout mStateView;

    private Toolbar mToolbar;
    private TextView mTxtTitle;
    private TextView mTxtMore;
    private FrameLayout mStatebar;

    private LoadingDialog mLoadingDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mContext = this;
        // Activity加入模拟栈
//        ActivityHelper.getInstance().addActivity(this);
        mStateView = (SRStateLayout) findViewById(R.id.state_view);
        int layout = getViewId();
        if (layout != 0) {
            mStateView.addStatLayout(SRStateLayout.STATE_SUCCESS, getViewId());
        }
        View view = mStateView.getStateView(SRStateLayout.STATE_SUCCESS);
        ButterKnife.bind(this, view);
        // 默认展示加载成功状态
        showSuccessStateLayout();
        // 初始化Toolbar标题栏
        initStatebar();
        initToolbar();
        // 初始化子类页面各个控件
        initView(savedInstanceState);
        // 初始化子类页面数据请求
        initData();

        // 标题栏用户中心图标
        initToolbarListener();
    }

    private void initToolbarListener() {
        setStatebarAlpha();
        setToolMoreIcon(R.drawable.ic_more, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(mContext, AboutActivity.class));
            }
        });
    }

    /**
     * 设置资源ID，子类必须重写
     *
     * @return
     */
    protected abstract int getViewId();

    /**
     * 设置初始化控件，子类必须重写
     *
     * @param savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 设置网络数据，子类可以重写
     */
    protected void initData() {
        // 空实现
    }

    /**
     * 显示加载进行中布局
     */
    protected void showLodingStateLayout() {
        mStateView.showStateView(SRStateLayout.STATE_LOADING);
    }

    /**
     * 显示加载成功布局
     */
    protected void showSuccessStateLayout() {
        mStateView.showStateView(SRStateLayout.STATE_SUCCESS);
    }

    /**
     * 显示加载错误/网络出错布局
     */
    protected void showErrorStateLayout() {
        mStateView.showStateView(SRStateLayout.STATE_ERROR);
    }

    /**
     * 显示数据为空布局
     */
    protected void showEmptyStateLayout() {
        mStateView.showStateView(SRStateLayout.STATE_EMPTY);
    }

    /**
     * 显示需要登录布局
     */
    protected void showLoginStateLayout() {
        mStateView.showStateView(SRStateLayout.STATE_LOGIN);
    }

    /**
     * 初始化沉浸式状态栏
     */
    private void initStatebar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mStatebar = (FrameLayout) findViewById(R.id.state_bar);
            mStatebar.setVisibility(View.VISIBLE);
            final int statusHeight = ScreenUtils.getStatusBarHeight(this);
            mStatebar.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams params = mStatebar.getLayoutParams();
                    params.height = statusHeight;
                    mStatebar.setLayoutParams(params);
                }
            });
        }
    }

    /**
     * 设置状态栏透明
     * 子类的布局往状态栏延伸，占据整个屏幕，系统时间依然可见
     */
    public void setStatebarAlpha() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStatebar.setVisibility(View.GONE);
        }
    }

    /**
     * 设置状态栏消失，全屏显示
     * 子类的布局往状态栏延伸，占据整个屏幕，但是系统时间没有了
     */
    public void setStatebarGone() {
        mStatebar.setVisibility(View.GONE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 初始化Toolbar标题栏
     *
     * @return
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTxtTitle = (TextView) findViewById(R.id.txt_title);
        mTxtMore = (TextView) findViewById(R.id.txt_more);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowTitleEnabled(false);
            }
            mToolbar.setNavigationIcon(R.drawable.ic_back);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    /**
     * 设置Toolbar中间的标题
     *
     * @param title
     */
    public void setToolbarTitle(String title) {
        if (!StringUtils.isNull(title)) {
            mTxtTitle.setText(title);
            mToolbar.setVisibility(View.VISIBLE);
        } else {
            mToolbar.setVisibility(View.GONE);
        }
    }

    /**
     * 设置Toolbar右边为文字及监听
     *
     * @param moreTxt
     * @param listener
     */
    public void setToolMoreText(String moreTxt, View.OnClickListener listener) {
        if (!StringUtils.isNull(moreTxt) && listener != null) {
            mTxtMore.setText(moreTxt);
            mTxtMore.setOnClickListener(listener);
        }
    }

    /**
     * 设置Toolbar右边为图标及监听
     *
     * @param moreRes
     * @param listener
     */
    public void setToolMoreIcon(int moreRes, View.OnClickListener listener) {
        if (moreRes != 0 && listener != null) {
            Drawable drawable = getResources().getDrawable(moreRes);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTxtMore.setCompoundDrawables(drawable, null, null, null);
            mTxtMore.setOnClickListener(listener);
        }
    }

    /**
     * 显示加载loading对话框
     *
     * @param message
     */
    public void showLoading(String message) {
        if (mLoadingDialog == null)
            mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.setTvLoading(message);
        mLoadingDialog.show();
    }

    /**
     * 消失加载loading对话框
     */
    public void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    /**
     * 提示用户的吐司信息
     *
     * @param message
     */
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Activity弹出模拟栈
//        ActivityHelper.getInstance().finishActivity(this);
    }
}

