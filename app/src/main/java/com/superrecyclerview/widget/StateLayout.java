package com.superrecyclerview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;

import com.superrecyclerview.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lilei on 2017/5/10.
 * <p/>
 * 状态：---------->界面布局
 * 正在加载 ----->layout_loading.xml
 * 加载错误 ----->state_layout_error.xmlor.xml
 * 数据为空 ----->state_layout_empty.xmlty.xml
 * 加载成功 ----->  自定义：网络请求数据填充之后的
 */

public class StateLayout extends FrameLayout {

    public static final int STATE_LOADING = 0;
    public static final int STATE_ERROR = 1;
    public static final int STATE_EMPTY = 2;
    public static final int STATE_LOGIN = 3;
    public static final int STATE_SUCCESS = 4;


    @IntDef({STATE_LOADING, STATE_ERROR, STATE_EMPTY, STATE_LOGIN, STATE_SUCCESS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StateEnum {
    }

    public StateLayout(Context context) {
        this(context, null);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedstateLayout_stateArray = context.obtainStyledAttributes(attrs, R.styleable.stateLayout_state);
        int indexCount = typedstateLayout_stateArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            //0, 1, 2, 3, 4
            int stateValue = typedstateLayout_stateArray.getInteger(i, -1);
            addStatLayout(stateValue);
        }

        typedstateLayout_stateArray.recycle();

        /*
                通过AttributeSet attrs读取 xml app:stateLayout_state_success_layout="@layout/activity_splash"
                activity_splash 读取成功之后添加到成功的状态布局
         */
        TypedArray stateLayout_state_success_layoutTypedArray = context.obtainStyledAttributes(attrs, R.styleable.stateLayout_state_success);
        int resourceId = stateLayout_state_success_layoutTypedArray.getResourceId(0, -1);
        if (resourceId != -1) {

            addStatLayout(STATE_SUCCESS, resourceId);
        }

        stateLayout_state_success_layoutTypedArray.recycle();

       /* addStatLayout(STATE_LOADING, R.layout.state_layout_loding);
        addStatLayout(STATE_ERROR, R.layout.state_layout_error);
        addStatLayout(STATE_EMPTY, R.layout.state_layout_empty);*/
    }


    private void addStatLayout(int state) {

        switch (state) {
            case STATE_LOADING:
                addStatLayout(STATE_LOADING, R.layout.layout_loding);
                break;
            case STATE_ERROR:
                addStatLayout(STATE_ERROR, R.layout.layout_error);
                break;
            case STATE_EMPTY:
                addStatLayout(STATE_EMPTY, R.layout.layout_empty);
                break;
            case STATE_LOGIN:
                addStatLayout(STATE_LOGIN, R.layout.layout_login);
                break;
            default:
                break;
        }

    }

    /*
           Map<Key,Value>

           SparseArray特殊的Map,  key值是int类型的Map
     */
    private SparseArray<View> mStateViews = new SparseArray<>();

    public void addStatLayout(int state, @LayoutRes int stateContentLayoutRes) {

        addStateView(state, View.inflate(getContext(), stateContentLayoutRes, null));
    }

    private int mCurrentState = STATE_LOADING;

    public void addStateView(int state, @NonNull View stateView) {

        View preview = mStateViews.get(state);
        if (preview != null) {

            removeView(preview);
        }

        mStateViews.put(state, stateView);
        addView(stateView);

        stateView.setVisibility(state == mCurrentState ? VISIBLE : INVISIBLE);
    }


    /*

        控制当前状态显示的方法
     */

    public void showStateView(@StateEnum int state) {

        //隐藏之前的状态
        View preStateView = mStateViews.get(mCurrentState);
        if (preStateView != null) {

            preStateView.setVisibility(INVISIBLE);
        }


        //显示新的状态
        View stateView = mStateViews.get(state);
        if (stateView != null) {
            stateView.setVisibility(VISIBLE);
        }

        //更新当前的状态
        mCurrentState = state;
    }

    @StateEnum
    public int getCurrentState() {

        return mCurrentState;
    }


    /*

            外部需要根据状态获取对于的View
     */
    public View getStateView(@StateEnum int state) {

        return mStateViews.get(state);
    }
}
