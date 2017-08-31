package com.superrecyclerview.multitype.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.superrecyclerview.multitype.holder.BetterViewHolder;
import com.superrecyclerview.multitype.type.TypeFactory;
import com.superrecyclerview.multitype.type.Visitable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiu on 11/18/16.
 */
public class MainAdapter extends RecyclerView.Adapter<BetterViewHolder> {

    private TypeFactory mTypeFactory;
    private List<Visitable> mVisitables;

    public MainAdapter(TypeFactory typeFactory, List<Visitable> visitables) {
        mTypeFactory = typeFactory;
        mVisitables = (visitables != null) ? visitables : new ArrayList<Visitable>();
    }

    @Override
    public int getItemViewType(int position) {
        return mVisitables.get(position).type(mTypeFactory);
    }

    @Override
    public BetterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mTypeFactory.onCreateViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(viewType, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(BetterViewHolder holder, int position) {
        holder.bindItem(mVisitables.get(position));
    }

    @Override
    public int getItemCount() {
        return mVisitables.size();
    }
}
