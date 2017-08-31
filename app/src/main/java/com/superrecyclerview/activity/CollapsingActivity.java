package com.superrecyclerview.activity;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.superrecyclerview.R;
import com.superrecyclerview.adapter.SuperTestAdapter;
import com.superrecyclerview.base.BaseSwipeBackActivity;
import com.superrecyclerview.bean.TestBean;
import com.superrecyclerview.decoration.SpaceDecoration;
import com.superrecyclerview.interfaces.OnLoadMoreListener;
import com.superrecyclerview.interfaces.OnNetWorkErrorListener;
import com.superrecyclerview.interfaces.OnRefreshListener;
import com.superrecyclerview.recyclerview.LRecyclerView;
import com.superrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.superrecyclerview.base.BaseRecyclerAdapter;
import com.superrecyclerview.utils.CommonUtils;
import com.superrecyclerview.utils.DensityUtil;
import com.superrecyclerview.utils.MessageUtils;
import com.superrecyclerview.utils.NetworkUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class CollapsingActivity extends BaseSwipeBackActivity {

    //    @Bind(tv_location)
//    TextView mTvLocation;
//    @Bind(tv_apptitle)
//    TextView mTvApptitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.banner_view)
    Banner mBannerView;
    @Bind(R.id.recycler_view)
    LRecyclerView mRecyclerView;
    @Bind(R.id.fab_button)
    FloatingActionButton mFabButton;

    private boolean isToBot;
    private boolean isToTop;


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
        return R.layout.activity_collapsing;
    }

    @Override
    protected void init(View view) {
        showSuccessStateLayout();
        initToolbar();
        initLRecyclerView();
    }

    @Override
    protected void initData() {

    }

    private void initLRecyclerView() {
        //滚动监听
        mRecyclerView.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onScrollUp() {
            }

            @Override
            public void onScrollDown() {
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
                if (!isToBot && distanceY < DensityUtil.dip2px(CollapsingActivity.this, 300)) {
                    CommonUtils.startWebBar2Bottom(mFabButton);
                    isToBot = true;
                    isToTop = false;
                }
                if (!isToTop && distanceY >= DensityUtil.dip2px(CollapsingActivity.this, 300)) {
                    CommonUtils.startWebBar2Up(mFabButton);
                    isToTop = true;
                    isToBot = false;
                }
//                if (distanceY <= 0) {
//                    mToolbar.setBackgroundColor(Color.argb(0, 63, 81, 181));
//                } else if (distanceY > 0 && distanceY <= DensityUtil.dip2px(MainActivity.this, 100)) {
//                    float scale = (float) distanceY / DensityUtil.dip2px(MainActivity.this, 100);
//                    float alpha = (255 * scale);
//                    mToolbar.setBackgroundColor(Color.argb((int) alpha, 63, 81, 181));
//                } else {
//                    mToolbar.setBackgroundColor(Color.argb(255, 63, 81, 181));
//                }
            }

            @Override
            public void onScrollStateChanged(int state) {
                switch (state) {
                    case RecyclerView.SCROLL_STATE_IDLE://静止
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING://拖动
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING://滑翔
                        break;
                }
            }
        });

        //刷新监听
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
//                mAdapter.clear();
//                mLRecyclerViewAdapter.notifyDataSetChanged();//fix bug:crapped or attached views may not be recycled. isScrap:false isAttached:true
//                mCurrentCount = 0;
//                requestData();
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.refreshComplete(10);
                        mTestBeens.addAll(0, mTempBeens);
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
        StaggeredGridLayoutManager manager = new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        //设置数据适配器
        mAdapter = new SuperTestAdapter(mTestBeens);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        //设置条目分隔线
        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayout.HORIZONTAL, 20, ContextCompat.getColor(this, R.color.colorPrimary)));
        SpaceDecoration decoration = new SpaceDecoration(CommonUtils.dip2px(this, 5));
        decoration.setPaddingEdgeSide(false);
        decoration.setPaddingHeaderFooter(false);
        decoration.setPaddingStart(true);
        mRecyclerView.addItemDecoration(decoration);
//        mRecyclerView.addItemDecoration(new DividerDecoration(ContextCompat.getColor(this, R.color.colorPrimary), 20, 20, 20));

        //下拉刷新、自动加载
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

        mBannerView.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mBannerView.setBannerTitles(titles);
        mBannerView.setImages(imgs).setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                imageView.setImageResource(((int) path));
            }
        }).start();

        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View itemView, int position) {
                if (CommonUtils.isFastDoubleClick()) {
                    MessageUtils.showInfo(mContext, "点击太快，休息一会");
                } else {
                    MessageUtils.showInfo(mContext, mTestBeens.get(position).title);
                }
            }
        });

        mAdapter.setOnLongClickListener(new BaseRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(View itemView, int position) {
                mAdapter.delete(position);
            }
        });
    }

    private void initToolbar() {
//        mTvLocation.setVisibility(View.VISIBLE);
//        mTvApptitle.setText("");

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//是否显示导航按钮
            actionBar.setDisplayShowTitleEnabled(false);//是否显示标题
        }
        mToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_more));//设置三个点图标
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_menu));//设置导航按钮

        //标签栏
        mTabLayout.addTab(mTabLayout.newTab().setText("tab1"));
        mTabLayout.addTab(mTabLayout.newTab().setText("tab2"));
        mTabLayout.addTab(mTabLayout.newTab().setText("tab3"));

        //浮动操作按钮
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "FloatingActionButton", Snackbar.LENGTH_LONG)
                        .setAction("点我置顶", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mRecyclerView.scrollToPosition(0);
                            }
                        }).show();
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
                finish();
                break;
            case R.id.action_search:
                Toast.makeText(CollapsingActivity.this, "搜索", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
