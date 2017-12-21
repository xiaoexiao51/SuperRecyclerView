package com.superrecyclerview.bean;

/**
 * Created by MMM on 2017/12/21.
 */
public class TestBean implements Comparable<TestBean> {
    public int id;
    public int type; // content type
    public String group;
    public String title;
    public int imgRes;
    public String imgUrl;
    public int height;

    public TestBean() {
    }

    public TestBean(String title) {
        this.title = title;
    }

    public TestBean(String group, String title) {
        this.group = group;
        this.title = title;
    }

    public TestBean(String title, int height) {
        this.title = title;
        this.height = height;
    }

    @Override
    public int compareTo(TestBean testBean) {
        return group.compareTo(testBean.group);
    }
}