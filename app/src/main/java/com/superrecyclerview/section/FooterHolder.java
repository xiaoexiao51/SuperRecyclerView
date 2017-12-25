package com.superrecyclerview.section;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.superrecyclerview.R;

/**
 * Created by MMM on 2017/12/23.
 */
public class FooterHolder extends RecyclerView.ViewHolder {

    public TextView footMore;

    public FooterHolder(View itemView) {
        super(itemView);
        footMore = (TextView) itemView.findViewById(R.id.tv_more);
    }
}
