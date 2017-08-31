package com.superrecyclerview.recyclerview;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.superrecyclerview.R;
import com.superrecyclerview.interfaces.ILoadMoreFooter;

public class DefultFooter extends LinearLayout implements ILoadMoreFooter {

    private String loadingHint = "正在加载...";
    private String noMoreHint = "已加载全部";
    private String noNetWorkHint = "点击重新加载";
    protected State mState = State.Normal;
    private int hintColor = R.color.color_refresh;

    private View mLoadingView;
    private View mNetworkErrorView;
    private View mTheEndView;
    private ProgressBar mProgressView;
    private TextView mLoadingText;
    private TextView mNoMoreText;
    private TextView mNoNetWorkText;


    public DefultFooter(Context context) {
        super(context);
        init(context);
    }

    public DefultFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DefultFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        inflate(context, R.layout.l_footer, this);
        setOnClickListener(null);

        onReset();//初始为隐藏状态
    }

    public void setFooterViewHint(String str1, String str2, String str3) {
        loadingHint = str1;
        noMoreHint = str2;
        noNetWorkHint = str3;
    }

    public void setHintTextColor(int color) {
        hintColor = color;
    }

    public void setViewBackgroundColor(int color) {
        this.setBackgroundColor(ContextCompat.getColor(getContext(), color));
    }

    public State getState() {
        return mState;
    }

    public void setState(State status) {
        setState(status, true);
    }

    @Override
    public void onReset() {
        onComplete();
    }

    @Override
    public void onLoading() {
        setState(State.Loading);
    }

    @Override
    public void onComplete() {
        setState(State.Normal);
    }

    @Override
    public void onNoMore() {
        setState(State.NoMore);
    }

    @Override
    public View getFootView() {
        return this;
    }

    /**
     * 设置状态
     *
     * @param status
     * @param showView 是否展示当前View
     */
    public void setState(State status, boolean showView) {
        if (mState == status) {
            return;
        }
        mState = status;
        switch (status) {
            case Normal:
                setOnClickListener(null);
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(GONE);
                }
                if (mTheEndView != null) {
                    mTheEndView.setVisibility(GONE);
                }
                if (mNetworkErrorView != null) {
                    mNetworkErrorView.setVisibility(GONE);
                }
                break;
            case Loading:
                setOnClickListener(null);
                if (mTheEndView != null) {
                    mTheEndView.setVisibility(GONE);
                }
                if (mNetworkErrorView != null) {
                    mNetworkErrorView.setVisibility(GONE);
                }
                if (mLoadingView == null) {
                    ViewStub viewStub = (ViewStub) findViewById(R.id.loading_viewstub);
                    mLoadingView = viewStub.inflate();

                    mProgressView = (ProgressBar) mLoadingView.findViewById(R.id.loading_progressbar);
                    mLoadingText = (TextView) mLoadingView.findViewById(R.id.loading_text);
                }
                mLoadingView.setVisibility(showView ? VISIBLE : GONE);
                mProgressView.setVisibility(View.VISIBLE);
                mLoadingText.setText(TextUtils.isEmpty(loadingHint) ? "正在加载..." : loadingHint);
                mLoadingText.setTextColor(ContextCompat.getColor(getContext(), hintColor));
                break;
            case NoMore:
                setOnClickListener(null);
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(GONE);
                }
                if (mNetworkErrorView != null) {
                    mNetworkErrorView.setVisibility(GONE);
                }
                if (mTheEndView == null) {
                    ViewStub viewStub = (ViewStub) findViewById(R.id.end_viewstub);
                    mTheEndView = viewStub.inflate();

                    mNoMoreText = (TextView) mTheEndView.findViewById(R.id.loading_end_text);
                } else {
                    mTheEndView.setVisibility(VISIBLE);
                }
                mTheEndView.setVisibility(showView ? VISIBLE : GONE);
                mNoMoreText.setText(TextUtils.isEmpty(noMoreHint) ? "已加载全部" : noMoreHint);
                mNoMoreText.setTextColor(ContextCompat.getColor(getContext(), hintColor));
                break;
            case NetWorkError:
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(GONE);
                }
                if (mTheEndView != null) {
                    mTheEndView.setVisibility(GONE);
                }
                if (mNetworkErrorView == null) {
                    ViewStub viewStub = (ViewStub) findViewById(R.id.network_error_viewstub);
                    mNetworkErrorView = viewStub.inflate();
                    mNoNetWorkText = (TextView) mNetworkErrorView.findViewById(R.id.network_error_text);
                } else {
                    mNetworkErrorView.setVisibility(VISIBLE);
                }
                mNetworkErrorView.setVisibility(showView ? VISIBLE : GONE);
                mNoNetWorkText.setText(TextUtils.isEmpty(noNetWorkHint) ? "点击重新加载" : noNetWorkHint);
                mNoNetWorkText.setTextColor(ContextCompat.getColor(getContext(), hintColor));
                break;
            default:
                break;
        }
    }


    public enum State {
        Normal, NoMore, Loading, NetWorkError
    }
}