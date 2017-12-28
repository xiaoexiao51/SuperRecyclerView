package com.superrecyclerview.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.superrecyclerview.R;
import com.superrecyclerview.base.BaseSwipeBackActivity;
import com.superrecyclerview.bean.ProductBean;
import com.superrecyclerview.interfaces.OnLoadMoreListener;
import com.superrecyclerview.interfaces.OnNetWorkErrorListener;
import com.superrecyclerview.interfaces.OnRefreshListener;
import com.superrecyclerview.multitype.adapter.MainAdapter;
import com.superrecyclerview.multitype.holder.TypeFactoryList;
import com.superrecyclerview.multitype.type.Footer;
import com.superrecyclerview.multitype.type.Header;
import com.superrecyclerview.multitype.type.HotList;
import com.superrecyclerview.multitype.type.ProductList;
import com.superrecyclerview.multitype.type.Visitable;
import com.superrecyclerview.recyclerview.LRecyclerView;
import com.superrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.superrecyclerview.utils.CommonUtils;
import com.superrecyclerview.utils.MessageUtils;
import com.superrecyclerview.utils.NetworkUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.youth.banner.transformer.TabletTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * BetterViewHolder的运用
 * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2016/1125/6806.html
 */
public class MultiTypeActivity extends BaseSwipeBackActivity {

    @Bind(R.id.tv_location)
    TextView mTvLocation;
    @Bind(R.id.tv_apptitle)
    TextView mTvApptitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recycler_view)
    LRecyclerView mRecyclerView;
    @Bind(R.id.fab_button)
    FloatingActionButton mFabButton;

    private MainAdapter mMainAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<Visitable> mVisitables = new ArrayList<>();

    @Override
    protected int getViewId() {
        return R.layout.activity_multitype;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolbar();
        initListener();// 必须先调用监听，才能自动刷新
        initRecyclerView();
    }

    @Override
    protected void initData() {

    }

    private void initListener() {
        // 刷新监听
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mVisitables.clear();
                CommonUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.refreshComplete(10);
                        mVisitables.addAll(getData());
                        mLRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                CommonUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.refreshComplete(10);
                        if (!NetworkUtils.isNetAvailable(mContext)) {
                            mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                                @Override
                                public void reload() {
                                    CommonUtils.getHandler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mVisitables.addAll(getData());
                                            mLRecyclerViewAdapter.notifyDataSetChanged();
                                        }
                                    }, 1000);
                                }
                            });
                            return;
                        }
                        if (mVisitables.size() < 24) {
                            mVisitables.addAll(getData());
                            mLRecyclerViewAdapter.notifyDataSetChanged();
                        } else {
                            mRecyclerView.setNoMore(true);
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
        // 2、设置管理器和适配器
        mRecyclerView.setLayoutManager(manager);
        mMainAdapter = new MainAdapter(new TypeFactoryList(), mVisitables);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mMainAdapter);
        mLRecyclerViewAdapter.setSpanSizeLookup(new LRecyclerViewAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                Object item = mVisitables.get(position);
                return (item instanceof HotList || item instanceof ProductList
                        || item instanceof Header) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setNestedScrollingEnabled(false);

        // 3、设置分割线

        // 下拉刷新、自动加载
        mRecyclerView.setRefreshEnabled(true);
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.refresh();

        // 添加头部
        View headerView = LayoutInflater.from(this).inflate(R.layout.inflater_header, null);
        mLRecyclerViewAdapter.addHeaderView(headerView);

        // 轮播大图
        List<Integer> imgs = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        imgs.add(R.drawable.ic_mztu);
        imgs.add(R.drawable.ic_mztu);
        imgs.add(R.drawable.ic_mztu);
        titles.add("妹子图1");
        titles.add("妹子图2");
        titles.add("妹子图3");

        Banner mBanner = (Banner) headerView.findViewById(R.id.banner_view);
        mBanner.setBannerAnimation(TabletTransformer.class);
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mBanner.setBannerTitles(titles);
        mBanner.setImages(imgs).setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                imageView.setImageResource(((int) path));
            }
        }).start();

        // 更新轮播图
//        mBanner.update(imgs, titles);

        // 4、设置监听事件
        mLRecyclerViewAdapter.setOnItemClickListener(new LRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MessageUtils.showInfo(mContext, "共有种类：" + mVisitables.size());
            }
        });
    }

    private List<Visitable> getData() {
        List<Visitable> visitables = new ArrayList<>();
        visitables.add(new Header("热销女装"));
        visitables.add(new ProductList(getProducts()));
        visitables.add(new Footer("75532", "看一看"));

        visitables.add(new Header("推荐商品"));
        visitables.add(new HotList(getProducts()));
        visitables.add(new Footer("12358", "瞧一瞧"));

        visitables.add(new Header("抢购商品"));
        visitables.add(new ProductList(getProducts()));
        visitables.add(new Footer("54669", "瞄一眼"));

        visitables.add(new Header("畅销男装"));
        visitables.add(new HotList(getProducts()));
        visitables.add(new Footer("343673", "啪一扒"));

        return visitables;
    }

    private List<ProductBean> getProducts() {
        List<ProductBean> products = new ArrayList<>();
        products.add(new ProductBean("自定义妹子1", R.drawable.ic_mztu));
        products.add(new ProductBean("自定义妹子2", R.drawable.ic_mztu));
        products.add(new ProductBean("自定义妹子3", R.drawable.ic_mztu));
        products.add(new ProductBean("自定义妹子4", R.drawable.ic_mztu));
        products.add(new ProductBean("自定义妹子5", R.drawable.ic_mztu));
        products.add(new ProductBean("自定义妹子6", R.drawable.ic_mztu));
        return products;
    }

    private void initToolbar() {
        mTvLocation.setVisibility(View.GONE);
        mTvApptitle.setText("多样式分组");

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//是否显示导航按钮
            actionBar.setDisplayShowTitleEnabled(false);//是否显示标题
        }
        mToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_more));//设置三个点图标
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_back));//设置回退图标

        //浮动操作按钮
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "FloatingActionButton", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_search:
                MessageUtils.showInfo(mContext, "搜索");
                break;
        }
        return true;
    }
}
