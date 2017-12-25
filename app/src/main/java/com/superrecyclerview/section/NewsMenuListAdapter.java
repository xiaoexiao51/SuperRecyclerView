package com.superrecyclerview.section;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superrecyclerview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MMM on 2017/12/23.
 */
public class NewsMenuListAdapter extends SectionRecyclerAdapter
        <HeaderHolder, DescHolder, RecyclerView.ViewHolder> {

    public List<NewsMenu> mNewsMenus;
    private Context mContext;
    private LayoutInflater mInflater;

    private List showingDatas = new ArrayList<>();
    private List<List<NewsMenu.NewsInfo>> childDatas = new ArrayList<>();

    public NewsMenuListAdapter(Context context, List<NewsMenu> newsMenus) {
        mNewsMenus = newsMenus;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        setShowingDatas();
    }

    public void setData(List<NewsMenu> newsMenus) {
        mNewsMenus = newsMenus;
        notifyDataSetChanged();
    }

    private void setShowingDatas() {
        showingDatas.clear();
        childDatas.clear();
        for (int i = 0; i < mNewsMenus.size(); i++) {
            String menuName = mNewsMenus.get(i).getMenuName();
            boolean isExpand = mNewsMenus.get(i).isExpand();
            List<NewsMenu.NewsInfo> newsDatas = mNewsMenus.get(i).getNewsDatas();
            showingDatas.add(menuName);
            if (isExpand) {
                showingDatas.addAll(newsDatas);
            }
        }
    }

    public int getPositionGroup(int groupPos) {
        for (int i = 0; i < showingDatas.size(); i++) {
            Object item = showingDatas.get(i);
            if (item instanceof String) {
                if (item.equals(mNewsMenus.get(groupPos).getMenuName())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int getPositionChild(int section, int position) {
        for (int i = 0; i < showingDatas.size(); i++) {
            Object item = showingDatas.get(i);
            if (item instanceof NewsMenu.NewsInfo) {
                if (item.equals(mNewsMenus.get(section).getNewsDatas().get(position))) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void delete(int section, int position) {
        mNewsMenus.get(section).getNewsDatas().remove(position);
        setShowingDatas();
        notifyDataSetChanged();
//        notifyItemRemoved(position);
//        if (position != (mItems.size())) { // 如果移除的是最后一个，忽略
//            notifyItemRangeChanged(position, this.mItems.size() - position);
//        }
    }

    @Override
    protected int getSectionCount() {
        return mNewsMenus == null ? 0 : mNewsMenus.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        int count = mNewsMenus.get(section).getNewsDatas().size();
        if (count >= 0 && !mNewsMenus.get(section).isExpand()) {
            count = 0;
        }
        return mNewsMenus.get(section).getNewsDatas() == null ? 0 : count;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected HeaderHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return new HeaderHolder(mInflater.inflate(R.layout.adapter_section_title, parent, false));
    }

    @Override
    protected DescHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new DescHolder(mInflater.inflate(R.layout.adapter_section_item, parent, false));
    }

    @Override
    protected void onBindSectionHeaderViewHolder(final HeaderHolder holder, final int section) {
        NewsMenu newsMenu = mNewsMenus.get(section);
        holder.tvGroup.setText(newsMenu.getMenuName());
        holder.tvExpand.setText(newsMenu.isExpand() ? "关闭" : "展开");

        // 展开关闭监听
        holder.tvGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isExpand = mNewsMenus.get(section).isExpand();
                holder.tvExpand.setText(isExpand ? "展开" : "关闭");
                mNewsMenus.get(section).setExpand(!isExpand);
                setShowingDatas();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onBindItemViewHolder(final DescHolder holder, final int section, final int position) {
        NewsMenu newsMenu = mNewsMenus.get(section);
        List<NewsMenu.NewsInfo> newsDatas = newsMenu.getNewsDatas();
        NewsMenu.NewsInfo newsInfo = newsDatas.get(position);
        holder.tvChild.setText(newsInfo.getTitle());

        // 设置监听事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(holder.itemView, section, position);
                }
            });
        }

        if (mItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mItemLongClickListener.onItemLongClick(holder.itemView, section, position);
                    return true;
                }
            });
        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int section, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View itemView, int section, int position);
    }
}
