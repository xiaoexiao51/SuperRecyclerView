package com.superrecyclerview.activity;


import android.animation.Animator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.superrecyclerview.R;
import com.superrecyclerview.base.BaseSwipeBackActivity;
import com.superrecyclerview.decoration.RecyclerViewDivider;
import com.superrecyclerview.expandable.base.BaseRecyclerViewAdapter;
import com.superrecyclerview.expandable.bean.RecyclerViewData;
import com.superrecyclerview.expandable.sample.BookAdapter;
import com.superrecyclerview.expandable.sample.BookBean;
import com.superrecyclerview.utils.AnimHelper;
import com.superrecyclerview.utils.DensityUtil;
import com.superrecyclerview.widget.QuickIndexBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class ExpandableActivity extends BaseSwipeBackActivity {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_location)
    TextView mTvLocation;
    @Bind(R.id.tv_apptitle)
    TextView mTvApptitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.qb_indexbar)
    QuickIndexBar mQbIndexbar;
    @Bind(R.id.tv_showletter)
    TextView mTvShowletter;

    @Bind(R.id.tv_love)
    TextView mTvLove;
    @Bind(R.id.fl_love)
    FrameLayout mFlLove;

    private List<RecyclerViewData> mDatas = new ArrayList<>();
    private BookAdapter mAdapter;
    private LinearLayoutManager mManager;
    private Animator mLoveAnimator;

    @Override
    protected int getViewId() {
        return R.layout.activity_expandable;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        showSuccessStateLayout();
        //获取数据
        initBooks();
        initQuickIndexBar();
        initLRecyclerView();
    }

    private void initLRecyclerView() {
        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setNestedScrollingEnabled(false);
//        mRecyclerView.addItemDecoration(new GroupDecoration(this, DensityUtil.dip2px(this, 10)));
        mRecyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayout.HORIZONTAL,
                DensityUtil.dip2px(this, 10), ContextCompat.getColor(this, R.color.color_bg)));

        mAdapter = new BookAdapter(this, mDatas);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onGroupItemClick(int position, int groupPosition, View view) {
                String group = (String) mDatas.get(groupPosition).getGroupData();
                Toast.makeText(ExpandableActivity.this, "groupPos:" + groupPosition + " group:" + group, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildItemClick(int position, int groupPosition, int childPosition, View view) {
                BookBean bean = (BookBean) mDatas.get(groupPosition).getChild(childPosition);
                Toast.makeText(ExpandableActivity.this, "groupPos:" + groupPosition + "  childPos:" + childPosition + " child:" + bean.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.setOnItemLongClickListener(new BaseRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onGroupItemLongClick(int position, int groupPosition, View view) {
                String group = (String) mDatas.get(groupPosition).getGroupData();
                showDeleteDialog(position, groupPosition, 0, true);
            }

            @Override
            public void onChildItemLongClick(int position, int groupPosition, int childPosition, View view) {
                BookBean bean = (BookBean) mDatas.get(groupPosition).getChild(childPosition);
                showDeleteDialog(position, groupPosition, childPosition, false);
            }
        });
    }

    @Override
    protected void initData() {

    }

    private int position;

    private void initQuickIndexBar() {
        mQbIndexbar.setOnTouchLetterListener(new QuickIndexBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                mTvShowletter.setVisibility(View.VISIBLE);
                mTvShowletter.setText(letter);
                for (int i = 0; i < mDatas.size(); i++) {
                    if (letter.equals(mDatas.get(i).getGroupData())) {
                        for (int j = 0; j < i; j++) {
                            List childDatas = mDatas.get(j).getGroupItem().getChildDatas();
                            position += childDatas.size() + 1;
                        }
                        mManager.scrollToPositionWithOffset(position, 0);
                        mManager.setStackFromEnd(true);
                        Logger.e("pos:" + position);
                        position = 0;
                    }
                }
            }

            @Override
            public void onLetterCancel() {
                mTvShowletter.setVisibility(View.INVISIBLE);
                mTvShowletter.setText("");
            }
        });
//        mQbIndexbar.setOnLetterUpdateListener(new QuickIndexBar.OnLetterUpdateListener() {
//            @Override
//            public void onLetterUpdate(String letter) {
//                mTvShowletter.setVisibility(View.VISIBLE);
//                mTvShowletter.setText(letter);
//            }
//
//            @Override
//            public void onLetterCancel() {
//                mTvShowletter.setVisibility(View.INVISIBLE);
//                mTvShowletter.setText("");
//            }
//        });
    }

    private void initBooks() {
        List<BookBean> bean1 = new ArrayList<>();
        List<BookBean> bean2 = new ArrayList<>();
        List<BookBean> bean3 = new ArrayList<>();
        List<BookBean> bean4 = new ArrayList<>();
        // id , pid , label , 其他属性
        bean1.add(new BookBean("文件管理系统"));
        bean1.add(new BookBean("游戏"));
        bean1.add(new BookBean("文档"));
        bean1.add(new BookBean("程序"));
        bean2.add(new BookBean("war3"));
        bean2.add(new BookBean("刀塔传奇"));

        bean1.add(new BookBean("面向对象"));
        bean2.add(new BookBean("非面向对象"));

        bean2.add(new BookBean("C++"));
        bean2.add(new BookBean("JAVA"));
        bean2.add(new BookBean("Javascript"));
        bean2.add(new BookBean("C"));

        bean3.add(new BookBean("文件管理系统"));
        bean3.add(new BookBean("游戏"));
        bean4.add(new BookBean("文档"));
        bean4.add(new BookBean("程序"));
        bean4.add(new BookBean("war3"));
        bean3.add(new BookBean("刀塔传奇"));

        bean3.add(new BookBean("面向对象"));
        bean4.add(new BookBean("非面向对象"));

        bean3.add(new BookBean("文件管理系统"));
        bean3.add(new BookBean("游戏"));
        bean4.add(new BookBean("文档"));
        bean4.add(new BookBean("程序"));
        bean4.add(new BookBean("war3"));
        bean4.add(new BookBean("刀塔传奇"));

        mDatas.add(new RecyclerViewData("A", bean1, true));
        mDatas.add(new RecyclerViewData("D", bean2, true));
        mDatas.add(new RecyclerViewData("M", bean3, true));
        mDatas.add(new RecyclerViewData("Q", bean4, true));
    }

    private void showDeleteDialog(final int position,
                                  final int groupPosition,
                                  final int childPosition,
                                  final boolean isGroup) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("提示!")
                .setMessage(isGroup ? "您确定要删除此组数据" : "您确定要删除此条数据")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //然后根据回调里的groupPosition和childPosition来更新你的数据源
                        if (isGroup) {
                            mDatas.remove(groupPosition);
                        } else {
                            mDatas.get(groupPosition).removeChild(childPosition);
                        }
                        mAdapter.notifyRecyclerViewData();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLoveAnimator == null) {
            mTvLove.post(new Runnable() {
                @Override
                public void run() {
                    mLoveAnimator = AnimHelper.doHappyJump(mTvLove, mTvLove.getHeight() / 2, 3000);
                }
            });
        } else {
            AnimHelper.startAnimator(mLoveAnimator);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        AnimHelper.stopAnimator(mLoveAnimator);
    }


    @OnClick(R.id.fl_love)
    public void onClick() {
        mTvLove.setBackgroundResource(R.drawable.ic_loved);
    }
}
