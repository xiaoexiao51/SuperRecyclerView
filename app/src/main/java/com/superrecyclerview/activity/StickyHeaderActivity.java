package com.superrecyclerview.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.superrecyclerview.R;
import com.superrecyclerview.adapter.StickyHeadAdapter;
import com.superrecyclerview.base.BaseRecyclerAdapter;
import com.superrecyclerview.base.BaseSwipeBackActivity;
import com.superrecyclerview.bean.TestBean;
import com.superrecyclerview.blur.BlurImageUtils;
import com.superrecyclerview.interfaces.OnLoadMoreListener;
import com.superrecyclerview.interfaces.OnNetWorkErrorListener;
import com.superrecyclerview.interfaces.OnRefreshListener;
import com.superrecyclerview.recyclerview.LRecyclerView;
import com.superrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.superrecyclerview.recyclerview.StartSnapHelper;
import com.superrecyclerview.utils.AnimHelper;
import com.superrecyclerview.utils.CommonUtils;
import com.superrecyclerview.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by MMM on 2017/12/25.
 */
public class StickyHeaderActivity extends BaseSwipeBackActivity {

    @Bind(R.id.recycler_view)
    LRecyclerView mRecyclerView;
    @Bind(R.id.iv_background)
    ImageView mIvBackground;

    private StickyHeadAdapter mAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<TestBean> mTestBeens = new ArrayList<>();
    private List<TestBean> mTempBeens = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;

    {
        mTestBeens.add(new TestBean("Z", "我们的纪念"));
        mTestBeens.add(new TestBean("B", "一个人的星光"));
        mTestBeens.add(new TestBean("A", "欠你的幸福"));
    }

    {
        mTempBeens.add(new TestBean("栏目一", "新增栏目一01"));
        mTempBeens.add(new TestBean("栏目二", "新增栏目二02"));
        mTempBeens.add(new TestBean("栏目三", "新增栏目三03"));
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_sticky_header;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initListener();// 必须先调用监听，才能自动刷新
        Collections.sort(mTestBeens);// 对数据进行排序
        initRecyclerView();

        //设置高斯模糊
        Glide.with(StickyHeaderActivity.this)
                .load(R.drawable.ic_mztu)
                .bitmapTransform(new BlurTransformation(StickyHeaderActivity.this, 25, 5))
                .into(mIvBackground);
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
                        int position = mLayoutManager.findFirstVisibleItemPosition();
                        int backResource;
                        if (position % 2 == 0) {
                            backResource = R.drawable.ic_splash;
                        } else {
                            backResource = R.drawable.ic_mztu;
                        }
                        //设置高斯模糊
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), backResource);
                        Bitmap blurImage = BlurImageUtils.getBlurBitmap(mContext, bitmap, 15); // 数字越大越模糊
                        AnimHelper.switchBackgroundAnim(mIvBackground, blurImage);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:// 拖动
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:// 滑翔
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
                            mAdapter.addAll(mTempBeens);
                            int position = mLayoutManager.findFirstVisibleItemPosition();
                            mRecyclerView.smoothScrollToPosition(position);
                        } else {
                            mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                                @Override
                                public void reload() {
                                    mRecyclerView.refreshComplete(10);
                                    mAdapter.addAll(mTempBeens);
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
        mLayoutManager = new LinearLayoutManager(mContext);
//        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(
//                1, StaggeredGridLayoutManager.VERTICAL);// 交错排列的Grid布局
        mAdapter = new StickyHeadAdapter(mTestBeens);
        // 2、设置管理器和适配器
        mRecyclerView.setLayoutManager(mLayoutManager);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
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
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                showToast(mTestBeens.get(position).title);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CommonUtils.getHandler().postDelayed(srollRunnable, 3000);
    }

    private Runnable srollRunnable = new Runnable() {
        @Override
        public void run() {
            int position = mLayoutManager.findFirstVisibleItemPosition();
            mRecyclerView.smoothScrollToPosition(position + 1);
            CommonUtils.getHandler().postDelayed(srollRunnable, 3000);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        CommonUtils.getHandler().removeCallbacks(srollRunnable);
    }
}
