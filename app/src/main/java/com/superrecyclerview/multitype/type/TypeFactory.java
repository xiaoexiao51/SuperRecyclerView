package com.superrecyclerview.multitype.type;

import android.view.View;

import com.superrecyclerview.multitype.holder.BetterViewHolder;

/**
 * 而工厂类也应该是个抽象类, 它包含了所需的类型.
 */
public interface TypeFactory {

    int type(Header header);

    int type(ProductList products);

    int type(HotList products);

    int type(Footer footer);

    BetterViewHolder onCreateViewHolder(View itemView, int viewType);
}
