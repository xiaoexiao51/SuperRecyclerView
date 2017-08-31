package com.superrecyclerview.activity;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.superrecyclerview.R;
import com.superrecyclerview.adapter.SuperTestAdapter;
import com.superrecyclerview.base.BaseSwipeBackActivity;
import com.superrecyclerview.bean.TestBean;
import com.superrecyclerview.decoration.DividerDecoration;
import com.superrecyclerview.interfaces.OnLoadMoreListener;
import com.superrecyclerview.interfaces.OnNetWorkErrorListener;
import com.superrecyclerview.interfaces.OnRefreshListener;
import com.superrecyclerview.recyclerview.LRecyclerView;
import com.superrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.superrecyclerview.stickyheader.StickyHeaderAdapter;
import com.superrecyclerview.stickyheader.StickyHeaderDecoration;
import com.superrecyclerview.utils.CommonUtils;
import com.superrecyclerview.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by MMM on 2017/8/4.
 */

public class StickyHeaderActivity extends BaseSwipeBackActivity {

    @Bind(R.id.recycler_view)
    LRecyclerView mRecyclerView;

    private SuperTestAdapter mAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<TestBean> mTestBeens = new ArrayList<>();
    private List<TestBean> mTempBeens = new ArrayList<>();

    {
        mTestBeens.add(new TestBean("欠你的幸福"));
        mTestBeens.add(new TestBean("谢谢爱"));
        mTestBeens.add(new TestBean("一个人的星光"));
        mTestBeens.add(new TestBean("终点"));
        mTestBeens.add(new TestBean("孤单心事"));
        mTestBeens.add(new TestBean("曾经太年轻"));
        mTestBeens.add(new TestBean("你的香气"));
        mTestBeens.add(new TestBean("远行"));
        mTestBeens.add(new TestBean("我不想忘记你"));
        mTestBeens.add(new TestBean("说爱我"));
        mTestBeens.add(new TestBean("我们的纪念"));
    }

    {
        mTempBeens.add(new TestBean("新增01"));
        mTempBeens.add(new TestBean("新增02"));
        mTempBeens.add(new TestBean("新增03"));
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_multitype;
    }

    @Override
    protected void init(View view) {
        showSuccessStateLayout();
        initLRecyclerView();
    }

    private void initLRecyclerView() {
        DividerDecoration itemDecoration = new DividerDecoration(Color.LTGRAY, CommonUtils.dip2px(this, 0.5f), CommonUtils.dip2px(this, 72), 0);
        itemDecoration.setDrawLastItem(false);
        mRecyclerView.addItemDecoration(itemDecoration);

        //刷新监听
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.refreshComplete(10);
                        mTestBeens.addAll(0, mTempBeens);
                        //fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
                        mLRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });
        //加载监听
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.refreshComplete(10);
                        if (NetworkUtils.isNetAvailable(mContext)) {
                            mAdapter.addAll(mTempBeens);
                        } else {
                            mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                                @Override
                                public void reload() {
                                    mRecyclerView.refreshComplete(10);
                                    mAdapter.addAll(mTempBeens);
                                }
                            });
                        }
                    }
                }, 1000);
//                if (mCurrentCount < TOTAL_COUNT) {
//                    // loading more
//                    requestData();
//                } else {
//                    //the end
//                    mRecyclerView.setNoMore(true);
//                }
            }
        });

        //设置布局管理器
//        StaggeredGridLayoutManager manager = new
//                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //设置数据适配器
        mAdapter = new SuperTestAdapter(mTestBeens);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        //下拉刷新、自动加载
        mRecyclerView.setRefreshEnabled(true);
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.refresh();

        //StickyHeader
        StickyHeaderDecoration decoration = new StickyHeaderDecoration(new StickyHeaderAdapter(this));
        decoration.setIncludeHeader(false);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void initData() {

    }
}
