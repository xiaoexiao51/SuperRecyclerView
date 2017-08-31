package com.superrecyclerview.adapter;

import android.widget.ImageView;

import com.superrecyclerview.R;
import com.superrecyclerview.base.SuperRecyclerAdapter;
import com.superrecyclerview.base.SuperViewHolder;
import com.superrecyclerview.bean.NewsBean;
import com.superrecyclerview.utils.GlideUtils;

import java.util.List;

/**
 * Created by MMM on 2017/8/8.
 */

public class NewsMainAdapter extends SuperRecyclerAdapter<NewsBean.T1348648517839Bean> {

    public NewsMainAdapter(List<NewsBean.T1348648517839Bean> items) {
        super(items);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.adapter_news_item;
    }

    @Override
    protected void onBindViewHolder(SuperViewHolder holder, int position, NewsBean.T1348648517839Bean item) {
        //绑定图片
        ImageView iv_cover = holder.getImageView(R.id.iv_cover);
//        GlideHelper.getInstance().injectImageWithNull(iv_cover, item.getImgsrc());
        GlideUtils.loadWithDefult(mContext, item.getImgsrc(), iv_cover);
        //绑定标题
        holder.getTextView(R.id.tv_test).setText(item.getTitle());
        //绑定浏览次数
        holder.getTextView(R.id.tv_count).setText(item.getReplyCount() + "");
    }
}
