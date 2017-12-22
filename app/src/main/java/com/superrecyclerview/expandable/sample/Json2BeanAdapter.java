package com.superrecyclerview.expandable.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superrecyclerview.R;
import com.superrecyclerview.expandable.base.ExRecyclerViewAdapter;
import com.superrecyclerview.expandable.bean.RecyclerViewData;

import java.util.List;

public class Json2BeanAdapter extends ExRecyclerViewAdapter<String, Json2Bean.FashiBean, Json2BeanViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private boolean isMultiCheck;

    public Json2BeanAdapter(Context context, List<RecyclerViewData> datas) {
        super(context, datas);
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setMultiCheckMode(boolean multiCheck) {
        this.isMultiCheck = multiCheck;
        multiCheck4AllGroup(multiCheck);
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
    public void onBindGroupHolder(final Json2BeanViewHolder holder, final int groupPos, int position, String groupData) {

        holder.tvGroup.setText(groupData);

        holder.groupCheck.setVisibility(isMultiCheck ? View.VISIBLE : View.INVISIBLE);

        boolean isAllCheck = isAllCheck4OneGroup(groupPos, true);
        holder.groupCheck.setSelected(isAllCheck);
        holder.groupCheck.setImageResource(isAllCheck ? R.drawable.check_box_bg_check : R.drawable.check_box_bg);

        holder.groupCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean selected = holder.groupCheck.isSelected();
                holder.groupCheck.setSelected(!selected);
                holder.groupCheck.setImageResource(selected ? R.drawable.check_box_bg : R.drawable.check_box_bg_check);
                multiCheck4OneGroup(groupPos, !selected);
            }
        });
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
    public void onBindChildpHolder(final Json2BeanViewHolder holder, final int groupPos, int childPos, final int position, final Json2Bean.FashiBean childData) {

        holder.tvChild.setText(childData.getName());

        holder.childCheck.setVisibility(isMultiCheck ? View.VISIBLE : View.INVISIBLE);

        boolean isCheck = childData.isCheck();
        childData.setCheck(isCheck);
        holder.childCheck.setImageResource(isCheck ? R.drawable.check_box_bg_check : R.drawable.check_box_bg);
        holder.tvChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isCheck = childData.isCheck();
                childData.setCheck(!isCheck);
                holder.childCheck.setImageResource(isCheck ? R.drawable.check_box_bg : R.drawable.check_box_bg_check);

                // 组员逐个选中至全选时点亮titleCheck，组员由全选至有一个没选时熄灭titleCheck
                if (isAllCheck4OneGroup(groupPos, true) || isAllCheck4OneGroup(groupPos, false)) {
                    notifyDataSetChanged();
                }
            }
        });

        holder.tvChild.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mLongClickListener != null) {
                    mLongClickListener.onLongClick(view, position);
                }
                if (!isMultiCheck) {
                    isMultiCheck = true;
                    childData.setCheck(true);
                    notifyDataSetChanged();
                }
                return true;
            }
        });
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
    public Json2BeanViewHolder createRealViewHolder(Context ctx, View view, int viewType) {
        return new Json2BeanViewHolder(ctx, view, viewType);
    }

    @Override
    public boolean canExpandAll() {
        return true;//true 全部可展开
    }

    @Override
    public boolean canExpandClick() {
        return true;//true 可点击展开
    }

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public interface OnItemLongClickListener {
        void onLongClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void setOnLongClickListener(OnItemLongClickListener listener) {
        mLongClickListener = listener;
    }
}
