package com.superrecyclerview.multitype.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.superrecyclerview.multitype.type.Visitable;

public abstract class BetterViewHolder<T extends Visitable> extends RecyclerView.ViewHolder {

    public BetterViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindItem(T t);
}
