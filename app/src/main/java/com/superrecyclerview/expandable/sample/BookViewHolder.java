package com.superrecyclerview.expandable.sample;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.superrecyclerview.R;
import com.superrecyclerview.expandable.base.BaseViewHolder;

/**
 * author：Drawthink
 * describe：
 * date: 2017/5/22
 */
public class BookViewHolder extends BaseViewHolder {

    public TextView tvName;
    public TextView tvTitle;

    public BookViewHolder(Context ctx, View itemView, int viewType) {
        super(ctx, itemView, viewType);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
    }

    @Override
    public int getGroupViewResId() {
        return R.id.ll_group;
    }

    @Override
    public int getChildViewResId() {
        return R.id.ll_child;
    }
}
