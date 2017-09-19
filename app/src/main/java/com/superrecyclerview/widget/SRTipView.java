package com.superrecyclerview.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superrecyclerview.R;
import com.superrecyclerview.utils.CommonUtils;

/**
 * 顶部提示
 */

public class SRTipView extends LinearLayout {

    private Context mContext;

    private int mBackColor;
    private int mTextColor;
    private String mText;
    private int mTextSize;
    private TextView mTvTip;

    //显示所停留的时间
    private int mStayTime = 2000;
    private boolean isShowing;
    private Handler mHandler = new Handler();

    public SRTipView(Context context) {
        this(context, null);
    }

    public SRTipView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SRTipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SRTipView);
        mBackColor = ta.getColor(R.styleable.SRTipView_tipBackColor, Color.parseColor("#ffffff"));
        mTextColor = ta.getColor(R.styleable.SRTipView_tipTextColor, Color.parseColor("#666666"));
        mText = ta.getString(R.styleable.SRTipView_tipText);
        mTextSize = ta.getDimensionPixelSize(R.styleable.SRTipView_tipTextSize, CommonUtils.sp2px(context, 13));
        ta.recycle();

        init();
    }

    private void init() {
        setGravity(Gravity.CENTER);
        setBackgroundColor(mBackColor);

        mTvTip = new TextView(mContext);
        mTvTip.setGravity(Gravity.CENTER);
        mTvTip.setText(mText);
        mTvTip.setTextColor(mTextColor);
        mTvTip.getPaint().setTextSize(mTextSize);

        addView(mTvTip);
    }

    public void show(String content) {
        if (TextUtils.isEmpty(content)) {
            show();
            return;
        }
        mTvTip.setText(content);//设置内容
        show();
    }

    public void show() {
        if (isShowing) {
            return;
        }

        isShowing = true;

        setVisibility(VISIBLE);

        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mTvTip, "scaleX", 0, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mTvTip, "scaleY", 0, 1f);

        set.setDuration(500);
        set.playTogether(scaleX, scaleY);
        set.start();

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hide();
                    }
                }, mStayTime);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 隐藏，收起
     */
    private void hide() {
        TranslateAnimation hideAnim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f);

        hideAnim.setDuration(300);
        startAnimation(hideAnim);
        hideAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(GONE);
                isShowing = false;
                mTvTip.setText(mText); //重新设置回原来的内容
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
