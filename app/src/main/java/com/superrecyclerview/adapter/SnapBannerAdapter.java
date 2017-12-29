package com.superrecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.superrecyclerview.R;
import com.superrecyclerview.bean.TestBean;
import com.superrecyclerview.utils.MessageUtils;

import java.util.List;

/**
 * Created by MMM on 2017/8/8.
 */
public class SnapBannerAdapter extends RecyclerView.Adapter
        <SnapBannerAdapter.BannerViewHolder> {

    private Context mContext;
    private List<TestBean> mTestBeens;

    public SnapBannerAdapter(List<TestBean> testBeens) {
        mTestBeens = testBeens;
    }

    public void setData(List<TestBean> testBeens) {
        this.mTestBeens = testBeens;
        notifyDataSetChanged();
    }

    @Override
    public BannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.adapter_snap_banner_item, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BannerViewHolder holder, int position) {
        position = position % mTestBeens.size();
        final TestBean testBean = mTestBeens.get(position);
        // 绑定标题
        holder.title.setText(testBean.title);
        // 绑定图片
        holder.cover.setImageResource(testBean.imgRes);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageUtils.showInfo(mContext, testBean.title);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView cover;

        BannerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_test);
            cover = (ImageView) itemView.findViewById(R.id.iv_cover);
        }
    }

//    public SnapBannerAdapter(List<TestBean> items) {
//        super(items);
//    }
//
//    @Override
//    protected int getLayoutId(int viewType) {
//        return R.layout.adapter_snap_banner_item;
//    }
//
//    @Override
//    protected void onBindViewHolder(BaseViewHolder holder, int position, TestBean item) {
//        int imageResource;
//        if (position % 2 == 0) {
//            imageResource = R.drawable.ic_splash;
//        } else {
//            imageResource = R.drawable.ic_mztu;
//        }
//        // 绑定图片
//        holder.getImageView(R.id.iv_cover).setImageResource(imageResource);
//        // 绑定标题
//        holder.getTextView(R.id.tv_test).setText(item.title);
//    }
}