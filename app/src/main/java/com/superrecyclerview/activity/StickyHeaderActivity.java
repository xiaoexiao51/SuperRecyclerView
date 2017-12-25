package com.superrecyclerview.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.superrecyclerview.R;
import com.superrecyclerview.adapter.StickyHeadAdapter;
import com.superrecyclerview.base.BaseRecyclerAdapter;
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
import com.superrecyclerview.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

/**
 * Created by MMM on 2017/12/21.
 */
public class StickyHeaderActivity extends BaseSwipeBackActivity {

    @Bind(R.id.recycler_view)
    LRecyclerView mRecyclerView;

    private StickyHeadAdapter mAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<TestBean> mTestBeens = new ArrayList<>();
    private List<TestBean> mTempBeens = new ArrayList<>();

    {
        mTestBeens.add(new TestBean("Z", "我们的纪念"));
        mTestBeens.add(new TestBean("E", "孤单心事"));
        mTestBeens.add(new TestBean("B", "一个人的星光"));
        mTestBeens.add(new TestBean("Z", "说爱我"));
        mTestBeens.add(new TestBean("A", "欠你的幸福"));
        mTestBeens.add(new TestBean("D", "终点"));
        mTestBeens.add(new TestBean("K", "远行"));
        mTestBeens.add(new TestBean("S", "我不想忘记你"));
        mTestBeens.add(new TestBean("B", "谢谢爱"));
        mTestBeens.add(new TestBean("E", "曾经太年轻"));
        mTestBeens.add(new TestBean("H", "你的香气"));
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
    }

    private void initListener() {
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
            }
        });
    }

    private void initRecyclerView() {
        // 1、创建管理器和适配器
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(
                1, StaggeredGridLayoutManager.VERTICAL);// 交错排列的Grid布局
        mAdapter = new StickyHeadAdapter(mTestBeens);
        // 2、设置管理器和适配器
        mRecyclerView.setLayoutManager(manager);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setNestedScrollingEnabled(false);

        // 3、设置分割线
//        SpaceDecoration decoration = new SpaceDecoration(CommonUtils.dip2px(this, 5));
//        decoration.setPaddingStart(true);
//        decoration.setPaddingEdgeSide(true);
//        decoration.setPaddingHeaderFooter(true);
//        decoration.isGroupRecyclerView(false);
//        mRecyclerView.addItemDecoration(decoration);

        DividerDecoration decoration1 = new DividerDecoration(ContextCompat.getColor(this, R.color.deep_line), 5);
        decoration1.setDrawLastItem(false);
        decoration1.setDrawHeaderFooter(false);
        mRecyclerView.addItemDecoration(decoration1);

//        mRecyclerView.addItemDecoration(new SimpleDecoration(this, LinearLayout.HORIZONTAL,
//                dip2px(this, 1), ContextCompat.getColor(this, R.color.color_bg)));

        // 下拉刷新、自动加载
        mRecyclerView.setRefreshEnabled(true);
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.refresh();

        // 粘性头部分组的实现
        StickyHeaderAdapter stickyHeaderAdapter = new StickyHeaderAdapter(this, mTestBeens);
        StickyHeaderDecoration decoration = new StickyHeaderDecoration(stickyHeaderAdapter);
        decoration.setIncludeHeader(false);
        mRecyclerView.addItemDecoration(decoration);

        // 4、设置监听事件
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                showToast(mTestBeens.get(position).title);
            }
        });
    }
}
