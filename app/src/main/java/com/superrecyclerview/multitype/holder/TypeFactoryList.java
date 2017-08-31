package com.superrecyclerview.multitype.holder;

import android.view.View;

import com.superrecyclerview.R;
import com.superrecyclerview.multitype.type.Footer;
import com.superrecyclerview.multitype.type.TypeFactory;
import com.superrecyclerview.multitype.type.Header;
import com.superrecyclerview.multitype.type.HotList;
import com.superrecyclerview.multitype.type.ProductList;

/**
 * 这样就完全是类型安全的实现, 不需要 Instance-of 的判断, 也不需要强制类型转化.
 * 对于每个具体工厂的实现也是清晰的, 实现对应的类型即可.
 */
public class TypeFactoryList implements TypeFactory {

    @Override
    public int type(Header category) {
        return R.layout.layout_section_header;
    }

    @Override
    public int type(ProductList products) {
        return R.layout.layout_section_items;
    }

    @Override
    public int type(HotList products) {
        return R.layout.layout_section_items2;
    }

    @Override
    public int type(Footer footer) {
        return R.layout.layout_section_footer;
    }

    @Override
    public BetterViewHolder onCreateViewHolder(View itemView, int viewType) {
        BetterViewHolder viewHolder = null;
        switch (viewType) {
            case R.layout.layout_section_header:
                viewHolder = new HeaderViewHolder(itemView);
                break;
            case R.layout.layout_section_items:
                viewHolder = new ProductsViewHolder(itemView);
                break;
            case R.layout.layout_section_items2:
                viewHolder = new HotsViewHolder(itemView);
                break;
            case R.layout.layout_section_footer:
                viewHolder = new FooterViewHolder(itemView);
                break;
            default:
                break;
        }
        return viewHolder;
    }
}
