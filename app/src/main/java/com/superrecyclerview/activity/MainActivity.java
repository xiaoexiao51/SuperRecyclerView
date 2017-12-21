package com.superrecyclerview.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.superrecyclerview.R;
import com.superrecyclerview.adapter.NewsMainAdapter;
import com.superrecyclerview.base.BaseActivity;
import com.superrecyclerview.base.BaseRecyclerAdapter;
import com.superrecyclerview.bean.NewsBean;
import com.superrecyclerview.decoration.SpaceDecoration;
import com.superrecyclerview.dialog.Rotate3dDialog;
import com.superrecyclerview.interfaces.OnLoadMoreListener;
import com.superrecyclerview.interfaces.OnNetWorkErrorListener;
import com.superrecyclerview.interfaces.OnRefreshListener;
import com.superrecyclerview.recyclerview.LRecyclerView;
import com.superrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.superrecyclerview.utils.ActivityUtils;
import com.superrecyclerview.utils.CommonUtils;
import com.superrecyclerview.utils.DensityUtil;
import com.superrecyclerview.utils.GlideUtils;
import com.superrecyclerview.utils.MessageUtils;
import com.superrecyclerview.utils.ScreenUtils;
import com.superrecyclerview.widget.SRPopWindow;
import com.superrecyclerview.widget.SRTipView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.youth.banner.transformer.ScaleInOutTransformer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

/**
 * Created by MMM on 2017/8/8.
 * MainActivity
 */
public class MainActivity extends BaseActivity {

    @Bind(R.id.tv_location)
    TextView mTvLocation;
    @Bind(R.id.tv_apptitle)
    TextView mTvApptitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.recycler_view)
    LRecyclerView mRecyclerView;
    @Bind(R.id.fab_button)
    FloatingActionButton mFabButton;
    @Bind(R.id.nav_view)
    NavigationView mNavView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.tv_tipview)
    SRTipView mTipView;
    @Bind(R.id.empty_view)
    LinearLayout mEmptyView;
    private Banner mBannerView;

    private boolean isToBot;
    private boolean isToTop;

    private int mCurrentPage = 0;
    private int mTotlePage = 5;
    private boolean isRefreshing = true;
    private boolean isLoading = false;

    private NewsMainAdapter mAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<NewsBean.T1348648517839Bean> mNewsBeens = new ArrayList<>();

    List<String> images = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    private SRPopWindow mPopWindow;

    @Override
    protected int getViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        showSuccessStateLayout();
        initToolbar();
        initListener();// 必须先调用监听，才能自动刷新
        initLRecyclerView();
    }

    private void initBannerView() {
        mBannerView.setBannerTitles(titles);
        mBannerView.setImages(images).setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
//                GlideHelper.getInstance().injectImageWithNull(imageView, ((String) path));
                GlideUtils.loadWithDefult(mContext, (String) path, imageView);
            }
        }).start();
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
                if (!isToBot && distanceY < DensityUtil.dip2px(MainActivity.this, 300)) {
                    CommonUtils.startWebBar2Bottom(mFabButton);
                    isToBot = true;
                    isToTop = false;
                }
                if (!isToTop && distanceY >= DensityUtil.dip2px(MainActivity.this, 300)) {
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

        // 刷新监听
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshing = true;
                mCurrentPage = 0;
                initData();
            }
        });
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mCurrentPage < mTotlePage) {
                    isLoading = true;
                    initData();
                } else {
                    mRecyclerView.setNoMore(true);
                }
            }
        });
    }

    private void initLRecyclerView() {
        // 1、创建管理器和适配器
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);// 交错排列的Grid布局
        mAdapter = new NewsMainAdapter(mNewsBeens);
        // 2、设置管理器和适配器
        mRecyclerView.setLayoutManager(manager);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setNestedScrollingEnabled(false);

        // 3、设置分割线
        SpaceDecoration decoration = new SpaceDecoration(CommonUtils.dip2px(this, 5));
        decoration.setPaddingStart(true);
        decoration.setPaddingEdgeSide(true);
        decoration.setPaddingHeaderFooter(true);
        decoration.isGroupRecyclerView(false);
        mRecyclerView.addItemDecoration(decoration);

//        DividerDecoration decoration1 = new DividerDecoration(ContextCompat.getColor(this, R.color.deep_line), 2);
//        decoration1.setDrawLastItem(false);
//        decoration1.setDrawHeaderFooter(false);
//        mRecyclerView.addItemDecoration(decoration1);

//        mRecyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayout.HORIZONTAL,
//                dip2px(this, 1), ContextCompat.getColor(this, R.color.color_bg)));

        // 下拉刷新、自动加载
        mRecyclerView.setRefreshEnabled(true);
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.refreshWithPull();

        // 添加头部
        View headerView = LayoutInflater.from(this).inflate(R.layout.inflater_header, null);
        mLRecyclerViewAdapter.addHeaderView(headerView);
        mBannerView = (Banner) headerView.findViewById(R.id.banner_view);
        mBannerView.setBannerAnimation(ScaleInOutTransformer.class);
        mBannerView.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);

        // 4、设置监听事件
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (CommonUtils.isFastDoubleClick()) {
                    MessageUtils.showInfo(mContext, "点击太快，休息一会");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("WEB_URL", mNewsBeens.get(position).getUrl_3w());
//                    bundle.putString("WEB_URL", "http://113.195.135.37:58080/NewShangRao/publish/126/2017-06-05/308.html");
                    bundle.putString("IMG_URL", mNewsBeens.get(position).getImgsrc());
                    bundle.putString("NEW_TIT", mNewsBeens.get(position).getTitle());
                    ActivityUtils.launchActivity(MainActivity.this, NewsDetailActivity.class, bundle);
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
        mTvLocation.setVisibility(View.VISIBLE);
        mTvApptitle.setText("");

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//是否显示导航按钮
            actionBar.setDisplayShowTitleEnabled(false);//是否显示标题
        }
        mToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_more));//设置三个点图标
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_menu));//设置导航按钮

        //侧滑菜单
        mNavView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                ActivityUtils.launchActivity(mContext, CollapsingActivity.class);
                                break;
                            case R.id.nav_messages:
                                ActivityUtils.launchActivity(mContext, MultiTypeActivity.class);
                                break;
                            case R.id.nav_friends:
                                ActivityUtils.launchActivity(mContext, ExpandableActivity.class);
                                break;
                            case R.id.nav_discussion:
                                ActivityUtils.launchActivity(mContext, StickyHeaderActivity.class);
                                break;
                            case R.id.sub_setting:
                                ActivityUtils.launchActivity(mContext, VideoPlayerActivity.class);
//                                new MaterialDialog.Builder(MainActivity.this)
//                                        .title("设置")
//                                        .content("这是要说什么")
//                                        .positiveText("同意")
//                                        .negativeText("拒绝")
//                                        .show();
                                break;
                            case R.id.sub_about:
                                new Rotate3dDialog(MainActivity.this).show();
//                                final MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this).title("说明").titleGravity(GravityEnum.CENTER).content("").show();
//                                dialog.getContentView().setText(Html.fromHtml("&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;此练习项目API来源于:高仿网易新闻客户端" + "<a href='https://github.com/tigerguixh/QuickNews'>QuickNews</a><br>"
//                                        + "&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;使用了诸如:<br>"
//                                        + "<a href='https://github.com/square/retrofit'>Retrofit2.0</a>,"
//                                        + "<a href='https://github.com/ReactiveX/RxJava'>RxJava</a>,"
//                                        + "<a href='https://github.com/greenrobot/greenDAO'>GreenDAO</a>,"
//                                        + "<a href='https://github.com/bumptech/glide'>Glide</a>,"
//                                        + "<a href='https://github.com/hongyangAndroid/AndroidChangeSkin'>AndroidChangeSkin</a>,"
//                                        + "<a href='https://github.com/Bilibili/ijkplayer'>Ijkplayer</a><br>等优秀开源项目。<br>"
//                                        + "&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;自己也自定义了刷新的控件，加载的控件，封装了RecyclerView的适配器，对MVP模式进行了基类的提取，用<a href='https://github.com/square/okhttp'>OkHttp</a>实现了请求缓存。<br>"
//                                        + "&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;App分为新闻、视频、图片三个模块，特色功能有换肤、侧滑返回。<br>"
//                                        + "&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;希望关注的朋友能与我交流学习。<br>"
//                                ));
                                break;
                        }
                        return true;
                    }
                });

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
        //获取搜索菜单
//        MenuItem item = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) item.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.action_search:
                MessageUtils.showInfo(mContext, "搜索吧");
                break;
            case R.id.action_more:
                final List<String> items = new ArrayList<>();
                items.add("发起群聊");
                items.add("添加朋友");
                items.add("扫一扫");
                items.add("收付款");
                items.add("帮助与反馈");
                mPopWindow = new SRPopWindow(mContext, items);
                mPopWindow.setPopWidth(CommonUtils.dip2px(mContext, 160));
                mPopWindow.showPopupWindow(mToolbar, ScreenUtils.getScreenWidth(mContext), 0);
                mPopWindow.setOnItemListener(new SRPopWindow.OnItemListener() {
                    @Override
                    public void OnItemListener(int position, String item) {
                        MessageUtils.showInfo(mContext, item);
                    }
                });
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBannerView.startAutoPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBannerView.stopAutoPlay();
    }

    private double exitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) < 2000) {
                overridePendingTransition(R.anim.hold, R.anim.zoom_out_exit);// 放大消失
                finish();
            } else {
                showToast("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initData() {
        Logger.e("加载第" + mCurrentPage + "页");
        String url = "http://c.3g.163.com/nc/article/list/T1348648517839/" +
                mCurrentPage * 20 + "-20.html";
        OkHttpUtils.get().url(url).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        Logger.e(response);
                        NewsBean newsBean = new Gson().fromJson(response, NewsBean.class);
                        List<NewsBean.T1348648517839Bean> bean = newsBean.getT1348648517839();
                        if (bean != null && bean.size() != 0) {
                            //绑定轮播图数据
                            titles.clear();
                            images.clear();
                            for (int i = 10; i < 15; i++) {
                                titles.add(bean.get(i).getTitle());
                                images.add(bean.get(i).getImgsrc());
                            }

                            initBannerView();

                            //绑定新闻列表数据
                            mCurrentPage++;
                            mRecyclerView.refreshComplete(20);

                            if (isRefreshing) {
                                isRefreshing = false;
                                mNewsBeens.clear();
                                mNewsBeens.addAll(bean);
                            }

                            if (isLoading) {
                                isLoading = false;
                                mAdapter.addAll(bean);
                                //mNewsBeens.addAll(bean);
                            }
                            mEmptyView.setVisibility(View.GONE);
                        } else {
                            mEmptyView.setVisibility(View.VISIBLE);
                            mRecyclerView.refreshComplete(20);
                            MessageUtils.showInfo(mContext, "没有最新内容");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logger.e(e.getMessage());
                        if (isRefreshing) {
                            mRecyclerView.refreshComplete(20);
                        }
                        if (isLoading) {
                            mRecyclerView.refreshComplete(20);
                            mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                                @Override
                                public void reload() {
                                    isLoading = true;
                                    initData();
                                }
                            });
                        }
                    }
                });
    }
}
