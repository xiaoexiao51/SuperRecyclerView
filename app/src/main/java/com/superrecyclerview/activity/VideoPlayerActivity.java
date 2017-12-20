package com.superrecyclerview.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.superrecyclerview.R;
import com.superrecyclerview.adapter.VideoPlayerAdapter;
import com.superrecyclerview.base.BaseSwipeBackActivity;
import com.superrecyclerview.bean.VideoBean;
import com.superrecyclerview.decoration.DividerDecoration;
import com.superrecyclerview.interfaces.OnLoadMoreListener;
import com.superrecyclerview.interfaces.OnNetWorkErrorListener;
import com.superrecyclerview.interfaces.OnRefreshListener;
import com.superrecyclerview.recyclerview.LRecyclerView;
import com.superrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.superrecyclerview.utils.CommonUtils;
import com.superrecyclerview.utils.MessageUtils;
import com.superrecyclerview.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.jzvd.JZVideoPlayer;

/**
 * Created by MMM on 2017/8/12.
 */

public class VideoPlayerActivity extends BaseSwipeBackActivity {

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

    private VideoPlayerAdapter mAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    public String[] videoThumbs = {
            "http://img4.jiecaojingxuan.com/2016/8/17/bd7ffc84-8407-4037-a078-7d922ce0fb0f.jpg",
            "http://img4.jiecaojingxuan.com/2016/8/17/f2dbd12e-b1cb-4daf-aff1-8c6be2f64d1a.jpg",
            "http://img4.jiecaojingxuan.com/2016/8/18/ccd86ca1-66c7-4331-9450-a3b7f765424a.png",
            "http://img4.jiecaojingxuan.com/2016/8/16/2adde364-9be1-4864-b4b9-0b0bcc81ef2e.jpg",
            "http://img4.jiecaojingxuan.com/2016/8/16/2a877211-4b68-4e3a-87be-6d2730faef27.png",
    };
    private List<VideoBean> mVideoItems = new ArrayList<>();

    {
        mVideoItems.add(new VideoBean(videoThumbs[0], "http://video.jiecao.fm/11/23/xu/%E5%A6%B9%E5%A6%B9.mp4"));
        mVideoItems.add(new VideoBean(videoThumbs[1], "http://video.jiecao.fm/8/17/%E6%8A%AB%E8%90%A8.mp4"));
        mVideoItems.add(new VideoBean(videoThumbs[2], "http://video.jiecao.fm/8/18/%E5%A4%A7%E5%AD%A6.mp4"));
        mVideoItems.add(new VideoBean(videoThumbs[3], "http://video.jiecao.fm/8/16/%E8%B7%B3%E8%88%9E.mp4"));
        mVideoItems.add(new VideoBean(videoThumbs[4], "http://video.jiecao.fm/8/16/%E9%B8%AD%E5%AD%90.mp4"));
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_video_player;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        showSuccessStateLayout();
        initToolbar();
        initRecyclerView();
    }

    private void initRecyclerView() {
        //设置管理器
        final StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        //滚动相关配置
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        //设置适配器
        mAdapter = new VideoPlayerAdapter(mVideoItems);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        DividerDecoration decoration1 = new DividerDecoration(ContextCompat.getColor(this, R.color.deep_line), 20);
        decoration1.setDrawHeaderFooter(false);
        decoration1.setDrawLastItem(false);
        mRecyclerView.addItemDecoration(decoration1);

        //刷新监听
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                CommonUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.refreshComplete(10);
                    }
                }, 1000);
            }
        });

        //加载更多
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
                                            mVideoItems.addAll(mVideoItems);
                                        }
                                    }, 1000);
                                }
                            });
                            return;
                        }
                        if (mVideoItems.size() < 24) {
                            mVideoItems.addAll(mVideoItems);
                        } else {
                            mRecyclerView.setNoMore(true);
                        }
                    }
                }, 1000);
            }
        });

        //自动刷新
        mRecyclerView.refresh();
        mRecyclerView.setRefreshEnabled(true);
        mRecyclerView.setLoadMoreEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    private void initToolbar() {
        mTvLocation.setVisibility(View.GONE);
        mTvApptitle.setText("视频播放");

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
