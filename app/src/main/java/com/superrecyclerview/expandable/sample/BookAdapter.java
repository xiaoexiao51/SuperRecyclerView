package com.superrecyclerview.expandable.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superrecyclerview.R;
import com.superrecyclerview.expandable.base.BaseRecyclerViewAdapter;
import com.superrecyclerview.expandable.bean.RecyclerViewData;

import java.util.List;


/**
 * author：Drawthink
 * describe：
 * date: 2017/5/22
 */
public class BookAdapter extends BaseRecyclerViewAdapter<String, BookBean, BookViewHolder> {

    private Context ctx;
    private LayoutInflater mInflater;

    public BookAdapter(Context ctx, List<RecyclerViewData> datas) {
        super(ctx, datas);
        mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
    }

    /**
     * head View数据设置
     *
     * @param holder
     * @param groupPos
     * @param position
     * @param groupData
     */
    @Override
    public void onBindGroupHolder(BookViewHolder holder, int groupPos, int position, String groupData) {
        holder.tvTitle.setText(groupData);
    }

    /**
     * child View数据设置
     *
     * @param holder
     * @param groupPos
     * @param childPos
     * @param position
     * @param childData
     */
    @Override
    public void onBindChildpHolder(BookViewHolder holder, int groupPos, int childPos, int position, BookBean childData) {
        holder.tvName.setText(childData.getName());
    }

    @Override
    public View getGroupView(ViewGroup parent) {
        return mInflater.inflate(R.layout.adapter_expand_title, parent, false);
    }

    @Override
    public View getChildView(ViewGroup parent) {
        return mInflater.inflate(R.layout.adapter_expand_item, parent, false);
    }

    @Override
    public BookViewHolder createRealViewHolder(Context ctx, View view, int viewType) {
        return new BookViewHolder(ctx, view, viewType);
    }

    @Override
    public boolean canExpandAll() {
        return true;//true 全部可展开
    }

    @Override
    public boolean canExpandClick() {
        return true;//true 可点击展开
    }
}
