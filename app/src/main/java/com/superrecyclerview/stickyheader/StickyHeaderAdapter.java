package com.superrecyclerview.stickyheader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.superrecyclerview.R;
import com.superrecyclerview.bean.TestBean;
import com.superrecyclerview.utils.StringUtils;

import java.util.List;

/**
 * Created by MMM on 2017/12/21.
 * 当前类注释：悬浮headerAdapter
 */
public class StickyHeaderAdapter implements StickyHeaderDecoration
        .IStickyHeaderAdapter<StickyHeaderAdapter.HeaderHolder> {

    private List<TestBean> mDatas;

    public StickyHeaderAdapter(Context context, List<TestBean> testBeens) {
        this.mDatas = testBeens;
    }

    @Override
    public long getHeaderId(int position) {
        //把数据放到控件上显示
        String firstLetter = mDatas.get(position).group;
        //分组
        if (position == 0) {
            //永远显示
            return 0;
        } else {
            //获取上一个好友条目的拼音首字母
            String preFirstLetter = mDatas.get(position - 1).group;
            if (StringUtils.isEqual(preFirstLetter, firstLetter)) {
                //相等，隐藏当前的拼音首字母
                return position - 1;
            } else {
                //不相等，显示当前的拼音首字母
                return position;
            }
        }
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.adapter_sticky_title, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder holder, int position) {
        holder.header.setText(mDatas.get(position).group);
    }

    protected class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView header;

        public HeaderHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView;
        }
    }
}
