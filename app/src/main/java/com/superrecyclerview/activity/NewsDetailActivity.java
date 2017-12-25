package com.superrecyclerview.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.superrecyclerview.R;
import com.superrecyclerview.base.BaseSwipeBackActivity;
import com.superrecyclerview.utils.CommonUtils;
import com.superrecyclerview.utils.GlideUtils;
import com.superrecyclerview.utils.MessageUtils;
import com.superrecyclerview.utils.StringUtils;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnImageClickListener;
import com.zzhoujay.richtext.ig.DefaultImageGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;

/**
 * Created by MMM on 2017/12/25.
 * 新闻详情页面
 */
public class NewsDetailActivity extends BaseSwipeBackActivity {

    @Bind(R.id.coor_layout)
    CoordinatorLayout mCoorLayout;
    @Bind(R.id.iv_cover)
    ImageView mIvCover;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ctl_layout)
    CollapsingToolbarLayout mCtlLayout;
    @Bind(R.id.fab_button)
    FloatingActionButton mFabButton;
    @Bind(R.id.rich_text)
    TextView mRichText;
    private String finalHtml;

    @Override
    protected int getViewId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //图片模糊处理
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_mztu);
//        Bitmap overlay = BlurImageUtils.blur(bitmap, 2, 2); //数字越大越模糊
//        mCoorLayout.setBackgroundDrawable(new BitmapDrawable(getResources(), overlay));

        //磁盘缓存
//        DiskCacheManager manager = new DiskCacheManager(this, "video_cache_file");
//        manager.put("video_cache", Serializable);
//        manager.getSerializable("video_cache");
        initToolbar();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        final String web_url = intent.getStringExtra("WEB_URL");
        final String img_url = intent.getStringExtra("IMG_URL");
        final String new_tit = intent.getStringExtra("NEW_TIT");
        //设置视差标题
        mCtlLayout.setTitle(new_tit);
//        GlideHelper.getInstance().injectImageWithNull(mIvCover, img_url);
        GlideUtils.loadWithDefult(mContext, img_url, mIvCover);

        if (!StringUtils.isNull(web_url)) {
            new HtmlAsyncTask().execute(web_url);
        }

//        if (!StringUtils.isNull(web_url)) {
//            // 开子线程解析web_url为html字符串
//            CommonUtils.runInThread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Document doc = Jsoup.connect(web_url).get();
//                        String html = doc.outerHtml();
//
////                        Elements pngs = doc.select("img[src]");
////                        for (Element element : pngs) {
////                            String imgUrl = element.attr("src");
////                            if (imgUrl.trim().startsWith("p")) {
////                                imgUrl = "http://113.195.135.37:58080/NewShangRao/" + imgUrl;
////                                element.attr("src", imgUrl);
////                            }
////                        }
////                        html = doc.toString();
//                        //截取body
//                        String startStr = "<article";
//                        String endedStr = "</article>";
////                        String startStr = "<body";
////                        String endedStr = "</body>";
//
//                        int startPos = html.indexOf(startStr);
//                        int endedPos = html.indexOf(endedStr);
//
//                        if (startPos != -1 && endedPos != -1) {
//                            html = html.substring(startPos, endedPos + endedStr.length());
//                        }
//
//                        // 把解析好的html字符串带到主线程
//                        finalHtml = html;
//                        CommonUtils.runOnUIThread(richTextRunnable);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
    }

    private Runnable richTextRunnable = new Runnable() {
        @Override
        public void run() {
//            if (NewsDetailActivity.this.isFinishing()) {
//                return;
//            }
//            Logger.e(finalHtml);
            RichText.from(finalHtml)
                    .autoFix(true)
                    .imageGetter(new DefaultImageGetter())
                    .imageClick(mImageClickListener)
                    .placeHolder(R.drawable.ic_download)
                    .into(mRichText);
        }
    };

    private OnImageClickListener mImageClickListener = new OnImageClickListener() {
        @Override
        public void imageClicked(List<String> imageUrls, int position) {
            MessageUtils.showInfo(mContext, imageUrls.get(position));
        }
    };

    private void initToolbar() {
        //mToolbar相关
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//是否显示导航按钮
            actionBar.setDisplayShowTitleEnabled(true);//是否显示标题
        }
//        mToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_more));//设置三个点图标
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_back));//设置导航按钮

        //浮动操作按钮
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "FloatingActionButton", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonUtils.getHandler().removeCallbacks(richTextRunnable);
    }

    // 开启线程之AsyncTask
    public class HtmlAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Document doc = null;
            try {
                doc = Jsoup.connect(strings[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String html = doc.outerHtml();

//            Elements pngs = doc.select("img[src]");
//            for (Element element : pngs) {
//                String imgUrl = element.attr("src");
//                if (imgUrl.trim().startsWith("p")) {
//                    imgUrl = "http://113.195.135.37:58080/NewShangRao/" + imgUrl;
//                    element.attr("src", imgUrl);
//                }
//            }
            html = doc.toString();
            //截取body
            String startStr = "<article";
            String endedStr = "</article>";
//            String startStr = "<body";
//            String endedStr = "</body>";

            int startPos = html.indexOf(startStr);
            int endedPos = html.indexOf(endedStr);

            if (startPos != -1 && endedPos != -1) {
                html = html.substring(startPos, endedPos + endedStr.length());
            }
            return html;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (StringUtils.isNull(s)) {
                return;
            }
            RichText.from(s)
                    .autoFix(true)
                    .imageGetter(new DefaultImageGetter())
                    .imageClick(mImageClickListener)
                    .placeHolder(R.drawable.ic_download)
                    .into(mRichText);
        }
    }
}
