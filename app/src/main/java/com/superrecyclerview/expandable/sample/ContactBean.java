package com.superrecyclerview.expandable.sample;

import java.util.List;

/**
 * Created by MMM on 2017/12/20.
 */
public class ContactBean {

    private List<DataListBean> dataList;

    public List<DataListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListBean> dataList) {
        this.dataList = dataList;
    }

    public static class DataListBean implements Comparable<DataListBean> {
        /**
         * friendjid : 释永信
         * status : 0
         * createuser : 1
         * createtime : Dec 20, 2017 5:01:00 PM
         * updateuser : 1
         * updatetime : Dec 20, 2017 5:01:00 PM
         * pageSize : 20
         * nickname : 释永信
         * headerimage : http://192.168.1.124:8080/upLoadPath/WellChatForBuddhism/20171219/9025213caa410f4b01261cb580e79fae.png
         */

        private String friendjid;
        private int status;
        private String createuser;
        private String createtime;
        private String updateuser;
        private String updatetime;
        private int pageSize;
        private String nickname;
        private String headerimage;
        private String pinyin;

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public String getFriendjid() {
            return friendjid;
        }

        public void setFriendjid(String friendjid) {
            this.friendjid = friendjid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreateuser() {
            return createuser;
        }

        public void setCreateuser(String createuser) {
            this.createuser = createuser;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getUpdateuser() {
            return updateuser;
        }

        public void setUpdateuser(String updateuser) {
            this.updateuser = updateuser;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeaderimage() {
            return headerimage;
        }

        public void setHeaderimage(String headerimage) {
            this.headerimage = headerimage;
        }

        @Override
        public int compareTo(DataListBean dataListBean) {
            return this.pinyin.compareTo(dataListBean.getPinyin());
        }
    }
}
