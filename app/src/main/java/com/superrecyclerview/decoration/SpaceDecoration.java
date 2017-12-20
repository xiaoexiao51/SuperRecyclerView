package com.superrecyclerview.decoration;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.superrecyclerview.expandable.base.BaseRecyclerViewAdapter;
import com.superrecyclerview.expandable.base.BaseViewHolder;
import com.superrecyclerview.recyclerview.LRecyclerViewAdapter;

/**
 * EasyRecyclerView
 */
public class SpaceDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int headerCount = -1;
    private int footerCount = Integer.MAX_VALUE;
    private boolean mPaddingStart = true;// 是否为顶部设置间距
    private boolean mPaddingEdgeSide = true;// 是否为两边设置间距
    private boolean mPaddingHeaderFooter = false;// 是否为头脚设置间距
    private boolean isGroupRecyclerView = false;// 是否为分组的列表

    public SpaceDecoration(int space) {
        this.space = space;
    }

    public void setPaddingStart(boolean mPaddingStart) {
        this.mPaddingStart = mPaddingStart;
    }

    public void setPaddingEdgeSide(boolean mPaddingEdgeSide) {
        this.mPaddingEdgeSide = mPaddingEdgeSide;
    }

    public void setPaddingHeaderFooter(boolean mPaddingHeaderFooter) {
        this.mPaddingHeaderFooter = mPaddingHeaderFooter;
    }

    public void isGroupRecyclerView(boolean isGroupRecyclerView) {
        this.isGroupRecyclerView = isGroupRecyclerView;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // 获取parent中item的位置信息
        int position = parent.getChildAdapterPosition(view);
        int spanCount = 0;
        int orientation = 0;
        int spanIndex = 0;
        int headerCount = 0;
        int footerCount = 0;

        // 计算LRecyclerView头脚的数量
        if (parent.getAdapter() instanceof LRecyclerViewAdapter) {
            headerCount = ((LRecyclerViewAdapter) parent.getAdapter()).getHeaderViewsCount() + 1;
            footerCount = ((LRecyclerViewAdapter) parent.getAdapter()).getFooterViewsCount();
        }

        // 获取parent的布局管理器
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
            spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        } else if (layoutManager instanceof GridLayoutManager) {
            orientation = ((GridLayoutManager) layoutManager).getOrientation();
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
            spanIndex = ((GridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        } else if (layoutManager instanceof LinearLayoutManager) {
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            spanCount = 1;
            spanIndex = 0;
        }

        // 计算每个item的大小以及上下左右的间距
        if ((position >= headerCount && position < parent.getAdapter().getItemCount() - footerCount)) {

            if (orientation == LinearLayout.VERTICAL) {
                float expectedWidth = (float) (parent.getWidth() - space * (spanCount + (mPaddingEdgeSide ? 1 : -1))) / spanCount;
                float originWidth = (float) parent.getWidth() / spanCount;
                float expectedX = (mPaddingEdgeSide ? space : 0) + (expectedWidth + space) * spanIndex;
                float originX = originWidth * spanIndex;

                if (parent.getAdapter() instanceof BaseRecyclerViewAdapter) {
                    int itemViewType = parent.getAdapter().getItemViewType(position);
                    if (itemViewType == BaseViewHolder.VIEW_TYPE_PARENT) {
                        outRect.left = 0;
                        outRect.right = 0;
                    } else {
                        outRect.left = (int) (expectedX - originX);
                        outRect.right = (int) (originWidth - outRect.left - expectedWidth);
                    }
                }else {
                    outRect.left = (int) (expectedX - originX);
                    outRect.right = (int) (originWidth - outRect.left - expectedWidth);
                }

                if (position - headerCount < spanCount && mPaddingStart && !isGroupRecyclerView) {
                    outRect.top = space;
                }
                outRect.bottom = space;
                return;
            } else {

                float expectedHeight = (float) (parent.getHeight() - space * (spanCount + (mPaddingEdgeSide ? 1 : -1))) / spanCount;
                float originHeight = (float) parent.getHeight() / spanCount;
                float expectedY = (mPaddingEdgeSide ? space : 0) + (expectedHeight + space) * spanIndex;
                float originY = originHeight * spanIndex;

                outRect.bottom = (int) (expectedY - originY);
                outRect.top = (int) (originHeight - outRect.bottom - expectedHeight);

                if (position - headerCount < spanCount && mPaddingStart) {
                    outRect.left = space;
                }
                outRect.right = space;
                return;
            }
        } else if (mPaddingHeaderFooter) {

            if (orientation == LinearLayout.VERTICAL) {
                outRect.right = outRect.left = mPaddingEdgeSide ? space : 0;
                outRect.top = mPaddingStart ? space : 0;
            } else {
                outRect.top = outRect.bottom = mPaddingEdgeSide ? space : 0;
                outRect.left = mPaddingStart ? space : 0;
            }
        }
    }
}