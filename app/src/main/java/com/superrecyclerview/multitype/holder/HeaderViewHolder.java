package com.superrecyclerview.multitype.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.superrecyclerview.R;
import com.superrecyclerview.multitype.type.Header;
import com.superrecyclerview.multitype.type.Visitable;
import com.superrecyclerview.utils.MessageUtils;

/**
 * Created by lizhixian on 2016/12/24.
 */
public class HeaderViewHolder extends BetterViewHolder {

    private Context mContext;
    private TextView mTvHeader;
    private TextView mTvMore;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        this.mContext = itemView.getContext();
        mTvHeader = (TextView) itemView.findViewById(R.id.tv_header);
        mTvMore = (TextView) itemView.findViewById(R.id.tv_more);
    }

    @Override
    public void bindItem(Visitable visitable) {
        final Header header = (Header) visitable;
        mTvHeader.setText(header.header);
        mTvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageUtils.showInfo(mContext, "Header:" + header.header);
            }
        });
    }
}
