package com.superrecyclerview.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.ImageView;

import com.superrecyclerview.R;
import com.superrecyclerview.adapter.SnapHeaderAdapter;
import com.superrecyclerview.base.BaseRecyclerAdapter;
import com.superrecyclerview.base.BaseSwipeBackActivity;
import com.superrecyclerview.bean.TestBean;
import com.superrecyclerview.blur.BlurImageUtils;
import com.superrecyclerview.interfaces.OnLoadMoreListener;
import com.superrecyclerview.interfaces.OnNetWorkErrorListener;
import com.superrecyclerview.interfaces.OnRefreshListener;
import com.superrecyclerview.recyclerview.LRecyclerView;
import com.superrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.superrecyclerview.recyclerview.SmoothLayoutManager;
import com.superrecyclerview.recyclerview.StartSnapHelper;
import com.superrecyclerview.utils.AnimHelper;
import com.superrecyclerview.utils.CommonUtils;
import com.superrecyclerview.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by MMM on 2017/12/25.
 */
public class SnapHeaderActivity extends BaseSwipeBackActivity {

    @Bind(R.id.recycler_view)
    LRecyclerView mRecyclerView;
    @Bind(R.id.iv_background)
    ImageView mIvBackground;

    private SnapHeaderAdapter mHeaderAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<TestBean> mTestBeens = new ArrayList<>();
    private List<TestBean> mTempBeens = new ArrayList<>();
    private SmoothLayoutManager mLayoutManager;

    {
        mTestBeens.add(new TestBean("我们的纪念", R.drawable.ic_splash));
        mTestBeens.add(new TestBean("一个人的星光", R.drawable.ic_mztu));
    }

    {
        mTempBeens.add(new TestBean("我们的纪念-新增", R.drawable.ic_splash));
        mTempBeens.add(new TestBean("一个人的星光-新增", R.drawable.ic_mztu));
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_snap_header;
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
        mRecyclerView.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {

            }

            @Override
            public void onScrollStateChanged(int state) {
                switch (state) {
                    case RecyclerView.SCROLL_STATE_IDLE:// 静止
                        // 继续滚动
                        CommonUtils.getHandler().postDelayed(scrollRunnable, 5000);

                        int position = mLayoutManager.findFirstVisibleItemPosition() - 1;
                        if (position >= 0) {
                            int imgRes = mTestBeens.get(position).imgRes;
                            // 高斯模糊
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgRes);
                            Bitmap blurImage = BlurImageUtils.getBlurBitmap(mContext, bitmap, 15);
                            AnimHelper.switchBackgroundAnim(mIvBackground, blurImage);
                        }
                        // 判断滚动是否到底
                        int visibleItemCount = mLayoutManager.getChildCount();
                        int totalItemCount = mLayoutManager.getItemCount();
                        int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                        if (visibleItemCount > 0
                                && lastVisibleItemPosition >= totalItemCount - 1
                                && totalItemCount > visibleItemCount) {
                            showToast("呀！玩命加载中！");
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

        // 刷新监听
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.refreshComplete(10);
                        mTestBeens.addAll(0, mTempBeens);
                        // fix bug:crapped or attached views may not be recycled.
                        // isScrap:false isAttached:true
                        mLRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.refreshComplete(10);
                        if (NetworkUtils.isNetAvailable(mContext)) {
                            mHeaderAdapter.addAll(mTempBeens);
                            int position = mLayoutManager.findFirstVisibleItemPosition();
                            mRecyclerView.smoothScrollToPosition(position);
                        } else {
                            mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                                @Override
                                public void reload() {
                                    mRecyclerView.refreshComplete(10);
                                    mHeaderAdapter.addAll(mTempBeens);
                                    int position = mLayoutManager.findFirstVisibleItemPosition();
                                    mRecyclerView.smoothScrollToPosition(position);
                                }
                            });
                        }
                    }
                }, 1000);
            }
        });
    }

    private void initRecyclerView() {
        // 1、创建管理器和适配器
        mLayoutManager = new SmoothLayoutManager(mContext);
//        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(
//                1, StaggeredGridLayoutManager.VERTICAL);// 交错排列的Grid布局
        mHeaderAdapter = new SnapHeaderAdapter(mTestBeens);
        // 2、设置管理器和适配器
        mRecyclerView.setLayoutManager(mLayoutManager);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mHeaderAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setNestedScrollingEnabled(false);

        // Snaphelper实现翻页效果
        SnapHelper pagerSnapHelper = new StartSnapHelper();
        pagerSnapHelper.attachToRecyclerView(mRecyclerView);

        // 3、设置分割线

        // 下拉刷新、自动加载
        mRecyclerView.setRefreshEnabled(true);
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.refresh();

        // 粘性头部分组的实现
//        StickyHeaderAdapter stickyHeaderAdapter = new StickyHeaderAdapter(this, mTestBeens);
//        StickyHeaderDecoration decoration = new StickyHeaderDecoration(stickyHeaderAdapter);
//        decoration.setIncludeHeader(false);
//        mRecyclerView.addItemDecoration(decoration);

        // 4、设置监听事件
        mHeaderAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
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