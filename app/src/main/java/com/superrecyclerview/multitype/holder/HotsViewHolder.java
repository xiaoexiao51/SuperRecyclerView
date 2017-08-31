package com.superrecyclerview.multitype.holder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.superrecyclerview.R;
import com.superrecyclerview.base.BaseRecyclerAdapter;
import com.superrecyclerview.multitype.adapter.HotsAdapter;
import com.superrecyclerview.multitype.type.HotList;
import com.superrecyclerview.multitype.type.Visitable;
import com.superrecyclerview.utils.MessageUtils;

/**
 * Created by MMM on 2017/8/8.
 * HotsViewHolder横向列表
 */
public class HotsViewHolder extends BetterViewHolder {

    private Context mContext;
    private RecyclerView recyclerView;

    public HotsViewHolder(View itemView) {
        super(itemView);
        this.mContext = itemView.getContext();
        recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(recyclerView);
//        SpaceDecoration decoration = new SpaceDecoration(CommonUtils.dip2px(mContext, 5));
//        recyclerView.addItemDecoration(decoration);
    }

    @Override
    public void bindItem(Visitable visitable) {
        final HotList hotList = (HotList) visitable;
        final HotsAdapter adapter = new HotsAdapter(hotList.products);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                MessageUtils.showInfo(mContext, hotList.products.get(position).title);
            }
        });
        adapter.setOnLongClickListener(new BaseRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(View itemView, int position) {
                adapter.delete(position);
            }
        });
    }
}
