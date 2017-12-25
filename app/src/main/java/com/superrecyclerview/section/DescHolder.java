package com.superrecyclerview.section;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.superrecyclerview.R;

/**
 * Created by MMM on 2017/12/23.
 */
public class DescHolder extends RecyclerView.ViewHolder {

    public TextView tvChild;

    public DescHolder(View itemView) {
        super(itemView);
        tvChild = (TextView) itemView.findViewById(R.id.tv_child);
    }
}
