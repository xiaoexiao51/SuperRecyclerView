package com.superrecyclerview.expandable.sample;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.superrecyclerview.R;
import com.superrecyclerview.expandable.base.BaseViewHolder;

public class Json2BeanViewHolder extends BaseViewHolder {

    public TextView tvGroup;
    public ImageView groupCheck;
    public TextView tvChild;
    public ImageView childCheck;

    public Json2BeanViewHolder(Context ctx, View itemView, int viewType) {
        super(ctx, itemView, viewType);
        tvGroup = (TextView) itemView.findViewById(R.id.tv_group);
        groupCheck = (ImageView) itemView.findViewById(R.id.group_check);
        tvChild = (TextView) itemView.findViewById(R.id.tv_child);
        childCheck = (ImageView) itemView.findViewById(R.id.child_check);
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
