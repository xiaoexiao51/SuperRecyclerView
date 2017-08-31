package com.superrecyclerview.multitype.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.superrecyclerview.R;
import com.superrecyclerview.multitype.type.Footer;
import com.superrecyclerview.multitype.type.Visitable;
import com.superrecyclerview.utils.MessageUtils;

/**
 * Created by lizhixian on 2016/12/24.
 */
public class FooterViewHolder extends BetterViewHolder {

    private Context mContext;
    private TextView mTvFooter;

    public FooterViewHolder(View itemView) {
        super(itemView);
        this.mContext = itemView.getContext();
        mTvFooter = (TextView) itemView.findViewById(R.id.tv_footer);
    }

    @Override
    public void bindItem(Visitable visitable) {
        final Footer footer = (Footer) visitable;
        mTvFooter.setText(footer.footer);
        mTvFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageUtils.showInfo(mContext, "Footer:商品id=" + footer.id);
            }
        });
    }
}