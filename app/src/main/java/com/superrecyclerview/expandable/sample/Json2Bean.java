package com.superrecyclerview.expandable.sample;

import java.util.List;

public class Json2Bean {

    private List<FashiBean> fashi;

    public List<FashiBean> getFashi() {
        return fashi;
    }

    public void setFashi(List<FashiBean> fashi) {
        this.fashi = fashi;
    }

    public static class FashiBean {
        /**
         * address : 浙江省杭州市
         * bytalkjid : 1512110427103507904
         * createtime : Dec 19, 2017 10:21:55 AM
         * createuser : 1
         * headerimage : http://192.168.1.124:8080/upLoadPath/WellChatForBuddhism/20171201/cb7e0ae07c9c06abc85b6af8c36c893c.png
         * name : 经他
         * pageSize : 20
         * phoneno : 18501592670
         * simiao : 灵隐寺
         * status : 0
         * updatetime : Dec 19, 2017 10:21:55 AM
         * updateuser : 1
         */

        private String address;
        private String bytalkjid;
        private String createtime;
        private String createuser;
        private String headerimage;
        private String name;
        private int pageSize;
        private String phoneno;
        private String simiao;
        private int status;
        private String updatetime;
        private String updateuser;
        private boolean isCheck;

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBytalkjid() {
            return bytalkjid;
        }

        public void setBytalkjid(String bytalkjid) {
            this.bytalkjid = bytalkjid;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getCreateuser() {
            return createuser;
        }

        public void setCreateuser(String createuser) {
            this.createuser = createuser;
        }

        public String getHeaderimage() {
            return headerimage;
        }

        public void setHeaderimage(String headerimage) {
            this.headerimage = headerimage;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public String getPhoneno() {
            return phoneno;
        }

        public void setPhoneno(String phoneno) {
            this.phoneno = phoneno;
        }

        public String getSimiao() {
            return simiao;
        }

        public void setSimiao(String simiao) {
            this.simiao = simiao;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getUpdateuser() {
            return updateuser;
        }

        public void setUpdateuser(String updateuser) {
            this.updateuser = updateuser;
        }
    }
}
