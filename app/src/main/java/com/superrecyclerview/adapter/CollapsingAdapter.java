package com.superrecyclerview.adapter;

import android.widget.LinearLayout;

import com.superrecyclerview.R;
import com.superrecyclerview.base.BaseRecyclerAdapter;
import com.superrecyclerview.base.BaseViewHolder;
import com.superrecyclerview.bean.TestBean;
import com.superrecyclerview.utils.CommonUtils;

import java.util.List;

/**
 * Created by MMM on 2017/8/8.
 */

public class CollapsingAdapter extends BaseRecyclerAdapter<TestBean> {

    public CollapsingAdapter(List<TestBean> items) {
        super(items);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.adapter_news_item;
    }

    @Override
    protected void onBindViewHolder(BaseViewHolder holder, int position, TestBean item) {
        // 绑定图片
        holder.getImageView(R.id.iv_cover).setImageResource(R.drawable.ic_mztu);
        // 绑定标题
        holder.getTextView(R.id.tv_test).setText(item.title);

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
