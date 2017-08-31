package com.superrecyclerview.expandable.base;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class GroupDecoration extends RecyclerView.ItemDecoration {

    private int mDividerHeight;

    public GroupDecoration(Context context, int dividerHeight) {
        mDividerHeight = dividerHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mDividerHeight;//类似加了一个bottom padding
    }
}