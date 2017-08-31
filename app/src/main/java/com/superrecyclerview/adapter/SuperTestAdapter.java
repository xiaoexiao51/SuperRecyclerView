package com.superrecyclerview.adapter;

import com.superrecyclerview.R;
import com.superrecyclerview.base.BaseRecyclerAdapter;
import com.superrecyclerview.base.BaseViewHolder;
import com.superrecyclerview.bean.TestBean;

import java.util.List;

/**
 * Created by MMM on 2017/8/8.
 */

public class SuperTestAdapter extends BaseRecyclerAdapter<TestBean> {

    public SuperTestAdapter(List<TestBean> items) {
        super(items);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.adapter_news_item;
    }

    @Override
    protected void onBindViewHolder(BaseViewHolder holder, int position, TestBean item) {
        holder.getTextView(R.id.tv_test).setText(item.title);
    }
}
