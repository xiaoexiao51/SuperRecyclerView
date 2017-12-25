package com.superrecyclerview.section;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.superrecyclerview.R;

/**
 * Created by MMM on 2017/12/23.
 */
public class HeaderHolder extends RecyclerView.ViewHolder {

    public TextView tvGroup;
    public TextView tvExpand;

    public HeaderHolder(View itemView) {
        super(itemView);
        tvGroup = (TextView) itemView.findViewById(R.id.tv_group);
        tvExpand = (TextView) itemView.findViewById(R.id.tv_expand);
    }
}
