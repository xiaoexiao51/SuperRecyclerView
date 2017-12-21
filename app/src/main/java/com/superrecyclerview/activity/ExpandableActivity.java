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
import android.widget.TextView;

import com.google.gson.Gson;
import com.superrecyclerview.R;
import com.superrecyclerview.base.BaseSwipeBackActivity;
import com.superrecyclerview.decoration.DividerDecoration;
import com.superrecyclerview.expandable.base.BaseRecyclerViewAdapter;
import com.superrecyclerview.expandable.bean.RecyclerViewData;
import com.superrecyclerview.expandable.sample.BookAdapter;
import com.superrecyclerview.expandable.sample.ContactBean;
import com.superrecyclerview.utils.AnimHelper;
import com.superrecyclerview.utils.CommonUtils;
import com.superrecyclerview.utils.StringUtils;
import com.superrecyclerview.widget.QuickIndexBar;

import java.util.ArrayList;
import java.util.Collections;
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
    private BookAdapter mBookAdapter;
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

    @Override
    protected void initData() {

    }

    private void initLRecyclerView() {
        // 1、创建管理器和适配器
        mManager = new LinearLayoutManager(this);
//        mManager = new GridLayoutManager(this, 2);
        mBookAdapter = new BookAdapter(this, mDatas);
//        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//
//                int itemViewType = mBookAdapter.getItemViewType(position);
//                if (itemViewType == BaseViewHolder.VIEW_TYPE_PARENT) {
//                    return 2;// 如果是标题，则占两个单元格
//                }
//                return 1;// 如果是内容，则占一个单元格
//            }
//        });
        // 2、设置管理器和适配器
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mBookAdapter);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setNestedScrollingEnabled(false);

        // 3、设置分割线
//        SpaceDecoration decoration = new SpaceDecoration(CommonUtils.dip2px(this, 1));
//        decoration.setPaddingStart(true);
//        decoration.setPaddingEdgeSide(true);
//        decoration.setPaddingHeaderFooter(true);
//        decoration.isGroupRecyclerView(true);
//        mRecyclerView.addItemDecoration(decoration);

        DividerDecoration decoration1 = new DividerDecoration(ContextCompat.getColor(this, R.color.deep_line), 2);
        decoration1.setDrawLastItem(false);
        decoration1.setDrawHeaderFooter(false);
        mRecyclerView.addItemDecoration(decoration1);

//        mRecyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayout.HORIZONTAL,
//                dip2px(this, 1), ContextCompat.getColor(this, R.color.color_bg)));

        // 4、设置监听事件
        mBookAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onGroupItemClick(int position, int groupPosition, View view) {
                String group = (String) mDatas.get(groupPosition).getGroupData();
                String groupText = "groupPos:" + groupPosition + " group:" + group;
                showToast(groupText);
            }

            @Override
            public void onChildItemClick(int position, int groupPosition, int childPosition, View view) {
                ContactBean.DataListBean bean = (ContactBean.DataListBean) mDatas.get(groupPosition).getChild(childPosition);
                String beanText = "groupPos:" + groupPosition + "  childPos:" + childPosition + " child:" + bean.getNickname();
                showToast(beanText);
            }
        });

        mBookAdapter.setOnItemLongClickListener(new BaseRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onGroupItemLongClick(int position, int groupPosition, View view) {
                String group = (String) mDatas.get(groupPosition).getGroupData();
                showDeleteDialog(position, groupPosition, 0, true);
            }

            @Override
            public void onChildItemLongClick(int position, int groupPosition, int childPosition, View view) {
                ContactBean.DataListBean bean = (ContactBean.DataListBean) mDatas.get(groupPosition).getChild(childPosition);
                showDeleteDialog(position, groupPosition, childPosition, false);
            }
        });
    }

    private int position;

    private void initQuickIndexBar() {
        mQbIndexbar.setOnTouchLetterListener(new QuickIndexBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                mTvShowletter.setVisibility(View.VISIBLE);
                mTvShowletter.setText(letter);
                if (mDatas != null && mDatas.size() != 0) {
                    for (int i = 0; i < mDatas.size(); i++) {
                        if (letter.equals(mDatas.get(i).getGroupData())) {
                            for (int j = 0; j < i; j++) {
                                List childDatas = mDatas.get(j).getGroupItem().getChildDatas();
                                position += childDatas.size() + 1;
                            }
                            mManager.scrollToPositionWithOffset(position, 0);
//                            mManager.setStackFromEnd(true);
                            position = 0;
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

    private void initBooks() {
        // 原始json字符串
        String jsonData = "{\"dataList\":[{\"friendjid\":\"释永信\",\"status\":0,\"createuser\":\"1\",\"createtime\":\"Dec 20, 2017 5:01:00 PM\",\"updateuser\":\"1\",\"updatetime\":\"Dec 20, 2017 5:01:00 PM\",\"pageSize\":20},{\"friendjid\":\"123\",\"status\":0,\"createuser\":\"1\",\"createtime\":\"Dec 20, 2017 5:01:00 PM\",\"updateuser\":\"1\",\"updatetime\":\"Dec 20, 2017 5:01:00 PM\",\"pageSize\":20},{\"friendjid\":\"1510108537217507904\",\"nickname\":\"释永信\",\"headerimage\":\"http://192.168.1.124:8080/upLoadPath/WellChatForBuddhism/20171219/9025213caa410f4b01261cb580e79fae.png\",\"status\":0,\"createuser\":\"1\",\"createtime\":\"Dec 20, 2017 5:01:00 PM\",\"updateuser\":\"1\",\"updatetime\":\"Dec 20, 2017 5:01:00 PM\",\"pageSize\":20},{\"friendjid\":\"1511942700462507904\",\"nickname\":\"释小龙\",\"headerimage\":\"http://192.168.1.124:8080/upLoadPath/WellChatForBuddhism/20171218/e702fac3dd4800abb014c42c1f28a44c.png\",\"status\":0,\"createuser\":\"1\",\"createtime\":\"Dec 20, 2017 5:01:00 PM\",\"updateuser\":\"1\",\"updatetime\":\"Dec 20, 2017 5:01:00 PM\",\"pageSize\":20},{\"friendjid\":\"1510047259167507904\",\"nickname\":\"123\",\"headerimage\":\"http://192.168.1.124:8080/upLoadPath/WellChatForBuddhism/20171108/91f25858a194b90fcdf4de7b1cea17a7.png\",\"status\":0,\"createuser\":\"1\",\"createtime\":\"Dec 20, 2017 5:01:00 PM\",\"updateuser\":\"1\",\"updatetime\":\"Dec 20, 2017 5:01:00 PM\",\"pageSize\":20},{\"friendjid\":\"1512110427103507904\",\"nickname\":\"经他\",\"headerimage\":\"http://192.168.1.124:8080/upLoadPath/WellChatForBuddhism/20171201/cb7e0ae07c9c06abc85b6af8c36c893c.png\",\"status\":0,\"createuser\":\"1\",\"createtime\":\"Dec 20, 2017 5:01:00 PM\",\"updateuser\":\"1\",\"updatetime\":\"Dec 20, 2017 5:01:00 PM\",\"pageSize\":20},{\"friendjid\":\"1513330325581507904\",\"nickname\":\"法海\",\"status\":0,\"createuser\":\"1\",\"createtime\":\"Dec 20, 2017 5:01:00 PM\",\"updateuser\":\"1\",\"updatetime\":\"Dec 20, 2017 5:01:00 PM\",\"pageSize\":20}]}";

        // 对数据进行加工
        ContactBean contactBean = new Gson().fromJson(jsonData, ContactBean.class);
        List<ContactBean.DataListBean> dataList = contactBean.getDataList();
        for (int i = 0; i < dataList.size(); i++) {
            String nickname = dataList.get(i).getNickname();
            String nickNamePinyin = CommonUtils.hanZiToPinyin(nickname);
            dataList.get(i).setPinyin(nickNamePinyin);
        }

        Collections.sort(dataList);

        boolean isGo = false;
        List<ContactBean.DataListBean> bean = null;

        for (int i = 0; i < dataList.size(); i++) {

            if (!isGo) {
                bean = new ArrayList<>();
                bean.add(dataList.get(i));
            }
            if (i + 1 < dataList.size()) {
                String firstLetter = dataList.get(i).getPinyin().charAt(0) + "";
                String nextLetter = dataList.get(i + 1).getPinyin().charAt(0) + "";
                if (StringUtils.isEqual(firstLetter, nextLetter)) {
                    bean.add(dataList.get(i + 1));
                    isGo = true;
                } else {
                    isGo = false;
                }
            } else {
                isGo = false;
            }
            if (!isGo) {
                String firstLetter = dataList.get(i).getPinyin().charAt(0) + "";
                mDatas.add(new RecyclerViewData(firstLetter, bean, true));
            }
        }

//        List<BookBean> bean1 = new ArrayList<>();
//        List<BookBean> bean2 = new ArrayList<>();
//        List<BookBean> bean3 = new ArrayList<>();
//        List<BookBean> bean4 = new ArrayList<>();
//        // id , pid , label , 其他属性
//        bean1.add(new BookBean("文件管理系统"));
//        bean1.add(new BookBean("游戏"));
//        bean1.add(new BookBean("文档"));
//        bean1.add(new BookBean("程序"));
//        bean2.add(new BookBean("war3"));
//        bean2.add(new BookBean("刀塔传奇"));
//
//        bean1.add(new BookBean("面向对象"));
//        bean2.add(new BookBean("非面向对象"));
//
//        bean2.add(new BookBean("C++"));
//        bean2.add(new BookBean("JAVA"));
//        bean2.add(new BookBean("Javascript"));
//        bean2.add(new BookBean("C"));
//
//        bean3.add(new BookBean("文件管理系统"));
//        bean3.add(new BookBean("游戏"));
//        bean4.add(new BookBean("文档"));
//        bean4.add(new BookBean("程序"));
//        bean4.add(new BookBean("war3"));
//        bean3.add(new BookBean("刀塔传奇"));
//
//        bean3.add(new BookBean("面向对象"));
//        bean4.add(new BookBean("非面向对象"));
//
//        bean3.add(new BookBean("文件管理系统"));
//        bean3.add(new BookBean("游戏"));
//        bean4.add(new BookBean("文档"));
//        bean4.add(new BookBean("程序"));
//        bean4.add(new BookBean("war3"));
//        bean4.add(new BookBean("刀塔传奇"));
//
//        mDatas.add(new RecyclerViewData("A", bean1, true));
//        mDatas.add(new RecyclerViewData("D", bean2, true));
//        mDatas.add(new RecyclerViewData("M", bean3, true));
//        mDatas.add(new RecyclerViewData("Q", bean4, true));
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
                        mBookAdapter.notifyRecyclerViewData();
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
