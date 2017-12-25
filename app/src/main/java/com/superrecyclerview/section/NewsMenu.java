package com.superrecyclerview.section;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MMM on 2017/12/23.
 */
public class NewsMenu implements Serializable {

    private String menuName;
    private List<NewsInfo> newsDatas;

    // 是否展开，默认展开
    private boolean isExpand = true;

    public NewsMenu(String menuName, List<NewsInfo> newsDatas) {
        this.menuName = menuName;
        this.newsDatas = newsDatas;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public List<NewsInfo> getNewsDatas() {
        return newsDatas;
    }

    public void setNewsDatas(List<NewsInfo> newsDatas) {
        this.newsDatas = newsDatas;
    }

    public static class NewsInfo {

        private String title;
        private String imgUrl;

        public NewsInfo(String title) {
            this.title = title;
            this.imgUrl = imgUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
