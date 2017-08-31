package com.superrecyclerview.multitype.adapter;

import android.widget.LinearLayout;

import com.superrecyclerview.R;
import com.superrecyclerview.base.BaseRecyclerAdapter;
import com.superrecyclerview.base.BaseViewHolder;
import com.superrecyclerview.bean.ProductBean;
import com.superrecyclerview.utils.CommonUtils;

import java.util.List;

public class HotsAdapter extends BaseRecyclerAdapter<ProductBean> {

    public HotsAdapter(List<ProductBean> items) {
        super(items);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.adapter_hots_item;
    }

    @Override
    protected void onBindViewHolder(BaseViewHolder holder, int position, ProductBean item) {
        //绑定封面
        holder.getImageView(R.id.iv_cover).setImageResource(item.coverres);
        //绑定标题
        holder.getTextView(R.id.tv_title).setText(item.title);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(CommonUtils.dip2px(mContext, 200), LinearLayout.LayoutParams.MATCH_PARENT);
        int margin_horizontal = 10;
        if (position == 0) {
            lp.setMargins(margin_horizontal, margin_horizontal / 2, margin_horizontal / 2, margin_horizontal / 2);
        } else if (position == mItems.size()) {
            lp.setMargins(margin_horizontal / 2, margin_horizontal / 2, margin_horizontal / 2, margin_horizontal);
        } else {
            lp.setMargins(margin_horizontal / 2, margin_horizontal / 2, margin_horizontal / 2, margin_horizontal / 2);
        }
        holder.itemView.setLayoutParams(lp);
    }
}

//    private List<ProductBean> products;
//    private Context context;
//
//    public void setData(List<ProductBean> products) {
//        this.products = products;
//    }
//
//    @Override
//    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        context = parent.getContext();
//        View view = LayoutInflater.from(context).inflate(R.layout.layout_section_item_hot, parent, false);
//        return new ProductViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ProductViewHolder holder, int position) {
//        final ProductBean product = products.get(position);
//        holder.cover.setImageResource(product.coverResId);
//        holder.title.setText(product.title);
//
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        int margin_horizontal = 20;
//        if (position % 2 == 0) {
//            lp.setMargins(margin_horizontal, margin_horizontal / 2, margin_horizontal / 2, margin_horizontal / 2);
//        } else {
//            lp.setMargins(margin_horizontal / 2, margin_horizontal / 2, margin_horizontal, margin_horizontal / 2);
//        }
//        holder.itemView.setLayoutParams(lp);
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MessageUtils.showInfo(context, product.title);
//            }
//        });
//
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                MessageUtils.showInfo(context, product.title);
//                return true;
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return products != null ? products.size() : 0;
//    }
//
//
//    public static class ProductViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView cover;
//        TextView title;
//
//        ProductViewHolder(View itemView) {
//            super(itemView);
//            cover = (ImageView) itemView.findViewById(R.id.cover);
//            title = (TextView) itemView.findViewById(R.id.title);
//        }
//    }
//}
