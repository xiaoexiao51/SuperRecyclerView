package com.superrecyclerview.bean;

/**
 * Created by MMM on 2017/7/30.
 */

public class TestBean {
    public int id;
    public int type; // content type
    public String title;
    public int imgRes;
    public String imgUrl;
    public int height;

    public TestBean() {
    }

    public TestBean(String title) {
        this.title = title;
    }

    public TestBean(String title, int height) {
        this.title = title;
        this.height = height;
    }
}