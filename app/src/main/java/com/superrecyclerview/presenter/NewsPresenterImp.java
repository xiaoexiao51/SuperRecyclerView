package com.superrecyclerview.presenter;

import com.superrecyclerview.bean.NewsBean;
import com.superrecyclerview.model.NewsModel;
import com.superrecyclerview.model.NewsModelImp;
import com.superrecyclerview.view.NewsView;

import java.util.List;

/**
 * Created by luo-pc on 2016/8/19.
 */
public class NewsPresenterImp implements NewsPresenter, NewsModelImp.OnLoadImageListListener {
    private NewsView imageLoadView;
    private NewsModel imageModel;

    public NewsPresenterImp(NewsView imageLoadView) {
        this.imageModel = new NewsModelImp();
        this.imageLoadView = imageLoadView;
    }

    @Override
    public void onSuccess(List<NewsBean> list) {
        imageLoadView.getImageList(list);
    }

    @Override
    public void onFailure(Exception e) {
        //我这里就不做处理了
    }

    @Override
    public void loadList(int pageIndex) {
        imageModel.LoadImageList(this, pageIndex);
    }

    public void onDestroy() {
        if (imageLoadView != null)
            imageLoadView = null;
    }
}
