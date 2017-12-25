package com.superrecyclerview.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.superrecyclerview.R;
import com.superrecyclerview.base.BaseSwipeBackActivity;
import com.superrecyclerview.decoration.DividerDecoration;
import com.superrecyclerview.interfaces.OnLoadMoreListener;
import com.superrecyclerview.interfaces.OnRefreshListener;
import com.superrecyclerview.recyclerview.LRecyclerView;
import com.superrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.superrecyclerview.section.NewsMenu;
import com.superrecyclerview.section.NewsMenuListAdapter;
import com.superrecyclerview.utils.CommonUtils;
import com.superrecyclerview.utils.StringUtils;
import com.superrecyclerview.widget.QuickIndexBar;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.youth.banner.transformer.ZoomOutSlideTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by MMM on 2017/12/23.
 */
public class SectionDemoActivity extends BaseSwipeBackActivity {

    @Bind(R.id.recycler_view)
    LRecyclerView mRecyclerView;

    @Bind(R.id.qb_indexbar)
    QuickIndexBar mQbIndexbar;
    @Bind(R.id.tv_showletter)
    TextView mTvShowletter;

    private NewsMenuListAdapter mMenuAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private StaggeredGridLayoutManager mManager;
    private List<NewsMenu> mNewsMenus = new ArrayList<>();

    @Override
    protected int getViewId() {
        return R.layout.activity_section_demo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initNewsMenu();
        initListener();// 必须先调用监听，才能自动刷新
        initQuickIndexBar();
        initLRecyclerView();
    }

    private void initNewsMenu() {
        List<NewsMenu.NewsInfo> bean1 = new ArrayList<>();
        List<NewsMenu.NewsInfo> bean2 = new ArrayList<>();
        List<NewsMenu.NewsInfo> bean3 = new ArrayList<>();
        List<NewsMenu.NewsInfo> bean4 = new ArrayList<>();
        // id , pid , label , 其他属性
        bean1.add(new NewsMenu.NewsInfo("文件管理系统"));
        bean1.add(new NewsMenu.NewsInfo("游戏"));
        bean1.add(new NewsMenu.NewsInfo("文档"));
        bean1.add(new NewsMenu.NewsInfo("程序"));
        bean2.add(new NewsMenu.NewsInfo("war3"));
        bean2.add(new NewsMenu.NewsInfo("刀塔传奇"));

        bean1.add(new NewsMenu.NewsInfo("面向对象"));
        bean2.add(new NewsMenu.NewsInfo("非面向对象"));

        bean2.add(new NewsMenu.NewsInfo("C++"));
        bean2.add(new NewsMenu.NewsInfo("JAVA"));
        bean2.add(new NewsMenu.NewsInfo("Javascript"));
        bean2.add(new NewsMenu.NewsInfo("C"));

        bean3.add(new NewsMenu.NewsInfo("文件管理系统"));
        bean3.add(new NewsMenu.NewsInfo("游戏"));
        bean4.add(new NewsMenu.NewsInfo("文档"));
        bean4.add(new NewsMenu.NewsInfo("程序"));
        bean4.add(new NewsMenu.NewsInfo("war3"));
        bean3.add(new NewsMenu.NewsInfo("刀塔传奇"));

        bean3.add(new NewsMenu.NewsInfo("面向对象"));
        bean4.add(new NewsMenu.NewsInfo("非面向对象"));

        bean3.add(new NewsMenu.NewsInfo("文件管理系统"));
        bean3.add(new NewsMenu.NewsInfo("游戏"));
        bean4.add(new NewsMenu.NewsInfo("文档"));
        bean4.add(new NewsMenu.NewsInfo("程序"));
        bean4.add(new NewsMenu.NewsInfo("war3"));
        bean4.add(new NewsMenu.NewsInfo("刀塔传奇"));

        mNewsMenus.add(new NewsMenu("A", bean1));
        mNewsMenus.add(new NewsMenu("D", bean2));
        mNewsMenus.add(new NewsMenu("M", bean3));
        mNewsMenus.add(new NewsMenu("Q", bean4));
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
//                        mTestBeens.addAll(0, mTempBeens);
//                        mLRecyclerViewAdapter.notifyDataSetChanged();
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
//                        if (NetworkUtils.isNetAvailable(mContext)) {
//                            mAdapter.addAll(mTempBeens);
//                        } else {
//                            mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
//                                @Override
//                                public void reload() {
//                                    mRecyclerView.refreshComplete(10);
//                                    mAdapter.addAll(mTempBeens);
//                                }
//                            });
//                        }
                    }
                }, 1000);
            }
        });
    }

    private void initLRecyclerView() {
        // 1、创建管理器和适配器
        mManager = new StaggeredGridLayoutManager(
                1, StaggeredGridLayoutManager.VERTICAL);// 交错排列的Grid布局
        mMenuAdapter = new NewsMenuListAdapter(mContext, mNewsMenus);
        // 2、设置管理器和适配器
        mRecyclerView.setLayoutManager(mManager);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mMenuAdapter);
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

        int color = ContextCompat.getColor(this, R.color.deep_line);
        int height = CommonUtils.dip2px(mContext, 1);
        DividerDecoration decoration1 = new DividerDecoration(color, height);
        decoration1.setDrawLastItem(true);
        decoration1.setDrawHeaderFooter(false);
        mRecyclerView.addItemDecoration(decoration1);

//        mRecyclerView.addItemDecoration(new SimpleDecoration(this, LinearLayout.HORIZONTAL,
//                dip2px(this, 1), ContextCompat.getColor(this, R.color.color_bg)));

        // 下拉刷新、自动加载
        mRecyclerView.setRefreshEnabled(true);
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.refresh();

        // 轮播大图
        List<Integer> imgs = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        imgs.add(R.drawable.ic_mztu);
        imgs.add(R.drawable.ic_mztu);
        imgs.add(R.drawable.ic_mztu);
        titles.add("妹子图1");
        titles.add("妹子图2");
        titles.add("妹子图3");

        // 添加头部
        View headerView = LayoutInflater.from(this).inflate(R.layout.inflater_header, null);
        mLRecyclerViewAdapter.addHeaderView(headerView);
        Banner mBannerView = (Banner) headerView.findViewById(R.id.banner_view);
        mBannerView.setBannerAnimation(ZoomOutSlideTransformer.class);
        mBannerView.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mBannerView.setBannerTitles(titles);
        mBannerView.setImages(imgs).setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                imageView.setImageResource(((int) path));
            }
        }).start();

        // 更新轮播图
//        mBanner.update(imgs, titles);

        // 4、设置监听事件
        mMenuAdapter.setOnItemClickListener(new NewsMenuListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int section, int position) {
                String title = mNewsMenus.get(section).getNewsDatas().get(position).getTitle();
                showToast("新闻标题：" + title);
            }
        });

        mMenuAdapter.setOnItemLongClickListener(new NewsMenuListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View itemView, int section, int position) {
                mMenuAdapter.delete(section, position);
            }
        });
    }

    private void initQuickIndexBar() {
        mQbIndexbar.setOnTouchLetterListener(new QuickIndexBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                mTvShowletter.setVisibility(View.VISIBLE);
                mTvShowletter.setText(letter);
                if (StringUtils.isEqual(letter, "#")) {
                    mManager.scrollToPositionWithOffset(0, 0);
                }
                if (mNewsMenus != null && mNewsMenus.size() != 0) {
//                    int position = 0;
                    for (int i = 0; i < mNewsMenus.size(); i++) {
                        if (letter.equals(mNewsMenus.get(i).getMenuName())) {
//                            for (int j = 0; j < i; j++) {
//                                List childDatas = mNewsMenus.get(j).getNewsDatas();
//                                position += childDatas.size() + 1;
//                            }
                            int positionGroup = mMenuAdapter.getPositionGroup(i);
                            mManager.scrollToPositionWithOffset(positionGroup + 2, 0);
                            showToast("字母" + letter + "索引定位在:" + positionGroup);
//                            mManager.setStackFromEnd(true);
                        }
                    }
                }
            }

            @Override
            public void onLetterCancel() {
                mTvShowletter.setVisibility(View.INVISIBLE);
                mTvShowletter.setText("");
            }
        });
    }
}
