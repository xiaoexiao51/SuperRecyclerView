package com.superrecyclerview.model;

import com.superrecyclerview.base.Constance;
import com.superrecyclerview.bean.NewsBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by luo-pc on 2016/8/19.
 */
public class NewsModelImp implements NewsModel {

    @Override
    public void LoadImageList(final OnLoadImageListListener listener, int pageIndex) {
        OkHttpUtils.get().url(Constance.URL + pageIndex * 20 + "-20.html").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                listener.onFailure(e);
            }

            @Override
            public void onResponse(String response, int id) {
//                List<NewsBean> imageBeen = JsonUtils.readJsonImageBean(response);
//                listener.onSuccess(imageBeen);
            }
        });
    }


    public interface OnLoadImageListListener {

        void onSuccess(List<NewsBean> list);

        void onFailure(Exception e);
    }
}
