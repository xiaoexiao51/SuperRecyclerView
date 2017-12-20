package com.superrecyclerview.adapter;

import com.superrecyclerview.R;
import com.superrecyclerview.base.BaseRecyclerAdapter;
import com.superrecyclerview.base.BaseViewHolder;
import com.superrecyclerview.bean.VideoBean;
import com.superrecyclerview.utils.GlideUtils;

import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by MMM on 2017/8/8.
 */

public class VideoPlayerAdapter extends BaseRecyclerAdapter<VideoBean> {

    public VideoPlayerAdapter(List<VideoBean> items) {
        super(items);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.adapter_video_player;
    }

    @Override
    protected void onBindViewHolder(BaseViewHolder holder, int position, VideoBean item) {

        ((JZVideoPlayerStandard) holder.getView(R.id.video_player)).setUp(item.getVideoUrl(),
                JZVideoPlayer.SCREEN_LAYOUT_LIST, "小视频00" + (position + 1));
        GlideUtils.loadWithDefult(mContext, item.getCoverUrl(),
                ((JZVideoPlayerStandard) holder.getView(R.id.video_player)).thumbImageView);
//        holder.jcVideoPlayer.setUp(
//                VideoConstant.videoUrls[0][position], JCVideoPlayer.SCREEN_LAYOUT_LIST,
//                VideoConstant.videoTitles[0][position]);
//        Picasso.with(holder.jcVideoPlayer.getContext())
//                .load(VideoConstant.videoThumbs[0][position])
//                .into(holder.jcVideoPlayer.thumbImageView);
//        //绑定图片
//        ImageView iv_cover = holder.getImageView(R.id.iv_cover);
//        GlideHelper.getInstances(mContext).injectImageWithNull(iv_cover, item.getImgsrc());
//        //绑定标题
//        holder.getTextView(R.id.tv_test).setText(item.getTitle());
//        //绑定浏览次数
//        holder.getTextView(R.id.tv_count).setText(item.getReplyCount() + "");
    }
}
