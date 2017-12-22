package com.superrecyclerview.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.superrecyclerview.R;
import com.superrecyclerview.base.BaseRecyclerAdapter;
import com.superrecyclerview.base.BaseViewHolder;
import com.superrecyclerview.bean.NewsBean;
import com.superrecyclerview.utils.CommonUtils;
import com.superrecyclerview.utils.GlideUtils;

import java.util.List;

/**
 * Created by MMM on 2017/8/8.
 */

public class MainNewsAdapter extends BaseRecyclerAdapter<NewsBean.T1348648517839Bean> {

    public MainNewsAdapter(List<NewsBean.T1348648517839Bean> items) {
        super(items);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.adapter_news_item;
    }

    @Override
    protected void onBindViewHolder(BaseViewHolder holder, int position, NewsBean.T1348648517839Bean item) {
        // 绑定图片
        ImageView iv_cover = holder.getImageView(R.id.iv_cover);
//        GlideHelper.getInstance().injectImageWithNull(iv_cover, item.getImgsrc());
        GlideUtils.loadWithDefult(mContext, item.getImgsrc(), iv_cover);
        // 绑定标题
        holder.getTextView(R.id.tv_test).setText(item.getTitle());
        // 绑定浏览次数
        holder.getTextView(R.id.tv_count).setText(item.getReplyCount() + "");

        // 重新计算每个条目之间的间距
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin_horizontal = CommonUtils.dip2px(mContext, 5);
        if (position == 0) {
            lp.setMargins(margin_horizontal, margin_horizontal, margin_horizontal / 2, margin_horizontal / 2);
        } else if (position == 1) {
            lp.setMargins(margin_horizontal / 2, margin_horizontal, margin_horizontal, margin_horizontal / 2);
        } else {
            if (position % 2 == 0) {
                lp.setMargins(margin_horizontal, margin_horizontal / 2, margin_horizontal / 2, margin_horizontal / 2);
            } else {
                lp.setMargins(margin_horizontal / 2, margin_horizontal / 2, margin_horizontal, margin_horizontal / 2);
            }
        }
        holder.itemView.setLayoutParams(lp);
    }
}
