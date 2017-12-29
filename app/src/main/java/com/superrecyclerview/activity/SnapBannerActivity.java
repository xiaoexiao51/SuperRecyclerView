package com.superrecyclerview.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.superrecyclerview.R;
import com.superrecyclerview.adapter.SnapBannerAdapter;
import com.superrecyclerview.base.BaseSwipeBackActivity;
import com.superrecyclerview.bean.TestBean;
import com.superrecyclerview.blur.BlurImageUtils;
import com.superrecyclerview.recyclerview.SmoothLayoutManager;
import com.superrecyclerview.recyclerview.StartSnapHelper;
import com.superrecyclerview.utils.AnimHelper;
import com.superrecyclerview.utils.CommonUtils;

import java.util.ArrayList;
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

    @Bind(R.id.ll_points)
    LinearLayout mLlPoints;

    private SnapBannerAdapter mBannerAdapter;
    private List<TestBean> mTestBeens = new ArrayList<>();
    private SmoothLayoutManager mLayoutManager;

    {
        mTestBeens.add(new TestBean("我们的纪念", R.drawable.ic_splash));
        mTestBeens.add(new TestBean("一个人的星光", R.drawable.ic_mztu));
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_snap_banner;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initListener();// 必须先调用监听，才能自动刷新
        initRecyclerView();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_splash);
        Bitmap blurImage = BlurImageUtils.getBlurBitmap(mContext, bitmap, 15);
        AnimHelper.switchBackgroundAnim(mIvBackground, blurImage);
    }

    private void initListener() {
        // 滚动监听
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:// 静止
                        // 继续滚动
                        CommonUtils.getHandler().postDelayed(scrollRunnable, 5000);

                        int position = mLayoutManager.findFirstVisibleItemPosition();
                        position = position % mTestBeens.size();
                        int backResource = mTestBeens.get(position).imgRes;
                        // 高斯模糊
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), backResource);
                        Bitmap blurImage = BlurImageUtils.getBlurBitmap(mContext, bitmap, 15);
                        AnimHelper.switchBackgroundAnim(mIvBackground, blurImage);

                        // 点亮小点
                        for (int i = 0; i < mTestBeens.size(); i++) {
                            ImageView imageView = (ImageView) mLlPoints.getChildAt(i);
                            if (i == position) {
                                imageView.setImageResource(R.drawable.ic_banner_seleted);
                            } else {
                                imageView.setImageResource(R.drawable.ic_banner_normal2);
                            }
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:// 拖动
                        // 停止滚动
                        CommonUtils.getHandler().removeCallbacks(scrollRunnable);
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:// 滑翔
                        // 停止滚动
                        CommonUtils.getHandler().removeCallbacks(scrollRunnable);
                        break;
                }
            }
        });
    }

    private void initRecyclerView() {
        // 1、创建管理器和适配器
        mLayoutManager = new SmoothLayoutManager(
                mContext, LinearLayoutManager.HORIZONTAL, false);
        mBannerAdapter = new SnapBannerAdapter(mTestBeens);
        // 2、设置管理器和适配器
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mBannerAdapter);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setNestedScrollingEnabled(false);

        // Snaphelper实现翻页效果
        SnapHelper pagerSnapHelper = new StartSnapHelper();
        pagerSnapHelper.attachToRecyclerView(mRecyclerView);

        // 滚动到MAX_VALUE值中间
        int mid = Integer.MAX_VALUE >> 1;
        int start = mid - mid % mTestBeens.size();
        mRecyclerView.scrollToPosition(start);

        addPoint();

        // 4、设置监听事件
//        mBannerAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View itemView, int position) {
//                showToast(mTestBeens.get(position).title);
//            }
//        });
    }

    private void addPoint() {
        mLlPoints.removeAllViews();
        for (int i = 0; i < mTestBeens.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.drawable.ic_banner_normal2);
            mLlPoints.addView(imageView);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();

            if (i != 0) {
                layoutParams.leftMargin = CommonUtils.dp2px(mContext, 10);
            }
        }

        // 点亮第一个小点
        ImageView childAt = (ImageView) mLlPoints.getChildAt(0);
        childAt.setImageResource(R.drawable.ic_banner_seleted);
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