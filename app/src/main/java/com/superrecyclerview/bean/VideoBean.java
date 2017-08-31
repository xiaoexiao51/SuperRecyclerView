package com.superrecyclerview.bean;

/**
 * Created by MMM on 2017/7/30.
 */

public class VideoBean {

    public String videoUrl;
    public String coverUrl;

    public VideoBean(String coverUrl, String videoUrl) {
        this.coverUrl = coverUrl;
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}