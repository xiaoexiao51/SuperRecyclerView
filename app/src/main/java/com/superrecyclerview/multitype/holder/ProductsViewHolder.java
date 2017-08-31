package com.superrecyclerview.multitype.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.superrecyclerview.R;
import com.superrecyclerview.base.BaseRecyclerAdapter;
import com.superrecyclerview.multitype.adapter.ProductsAdapter;
import com.superrecyclerview.multitype.type.ProductList;
import com.superrecyclerview.multitype.type.Visitable;
import com.superrecyclerview.utils.MessageUtils;

/**
 * Created by lizhixian on 2016/12/24.
 */
public class ProductsViewHolder extends BetterViewHolder {

    private Context mContext;
    private RecyclerView recyclerView;

    public ProductsViewHolder(View itemView) {
        super(itemView);
        this.mContext = itemView.getContext();
        recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
//        SpaceDecoration decoration = new SpaceDecoration(CommonUtils.dip2px(mContext, 5));
//        decoration.setPaddingEdgeSide(false);
//        recyclerView.addItemDecoration(decoration);
    }

    @Override
    public void bindItem(Visitable visitable) {
        final ProductList productList = (ProductList) visitable;
        final ProductsAdapter adapter = new ProductsAdapter(productList.products);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                MessageUtils.showInfo(mContext, productList.products.get(position).title);
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
