package com.superrecyclerview.model;

/**
 * Created by luo-pc on 2016/8/19.
 */
public interface NewsModel {
    void LoadImageList(NewsModelImp.OnLoadImageListListener listener, int pageIndex);
}
