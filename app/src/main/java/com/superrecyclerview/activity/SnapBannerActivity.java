package com.superrecyclerview.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.ImageView;

import com.superrecyclerview.R;
import com.superrecyclerview.adapter.SnapBannerAdapter;
import com.superrecyclerview.base.BaseRecyclerAdapter;
import com.superrecyclerview.base.BaseSwipeBackActivity;
import com.superrecyclerview.bean.TestBean;
import com.superrecyclerview.blur.BlurImageUtils;
import com.superrecyclerview.recyclerview.SmoothLayoutManager;
import com.superrecyclerview.recyclerview.StartSnapHelper;
import com.superrecyclerview.utils.AnimHelper;
import com.superrecyclerview.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

/**
 * Created by MMM on 2017/12/25.
 */
public class SnapBannerActivity extends BaseSwipeBackActivity {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.iv_background)
    ImageView mIvBackground;

    private SnapBannerAdapter mBannerAdapter;
    private List<TestBean> mTestBeens = new ArrayList<>();
    private SmoothLayoutManager mLayoutManager;

    {
        mTestBeens.add(new TestBean("Z", "我们的纪念"));
        mTestBeens.add(new TestBean("B", "一个人的星光"));
        mTestBeens.add(new TestBean("A", "欠你的幸福"));
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_snap_banner;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Collections.sort(mTestBeens);// 对数据进行排序
        initRecyclerView();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_splash);
        Bitmap blurImage = BlurImageUtils.getBlurBitmap(mContext, bitmap, 15);
        AnimHelper.switchBackgroundAnim(mIvBackground, blurImage);
    }

    private void initRecyclerView() {
        // 1、创建管理器和适配器
        mLayoutManager = new SmoothLayoutManager(
                mContext, LinearLayoutManager.HORIZONTAL, false);
//        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(
//                1, StaggeredGridLayoutManager.VERTICAL);// 交错排列的Grid布局
        mBannerAdapter = new SnapBannerAdapter(mTestBeens);
        // 2、设置管理器和适配器
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mBannerAdapter);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setNestedScrollingEnabled(false);

        // Snaphelper实现翻页效果
        SnapHelper pagerSnapHelper = new StartSnapHelper();
        pagerSnapHelper.attachToRecyclerView(mRecyclerView);

        // 3、设置分割线

        // 4、设置监听事件
        mBannerAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                showToast(mTestBeens.get(position).title);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.getHandler().postDelayed(scrollRunnable, 5000);
    }

    private Runnable scrollRunnable = new Runnable() {
        @Override
        public void run() {
            int position = mLayoutManager.findFirstVisibleItemPosition();
            mRecyclerView.smoothScrollToPosition(position + 1);
            CommonUtils.getHandler().postDelayed(scrollRunnable, 5000);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        CommonUtils.getHandler().removeCallbacks(scrollRunnable);
    }
}