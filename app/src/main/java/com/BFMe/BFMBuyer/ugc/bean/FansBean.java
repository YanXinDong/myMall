package com.BFMe.BFMBuyer.ugc.bean;

import java.util.List;

/**
 * Description：
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/19 16:13
 */
public class FansBean {

    /**
     * FansCount : 2
     * Id : 0
     * Photo : http://img01.baifomi.com//Storage/Member/40764/a65fdc08762abab6.jpeg
     * TopicCount : 20
     * UserId : Qshx087vnhDrjv0d2B8dhB5VU9seC4mXrAUqKGX1Drq+jbo/yHlIocmmBDKrSmPi+95DcH3vmUBXjN+n2NCNKfOvN4Wrp4QtTmTUS+M4QMeY/uUFyUyWXLghg6vFs4gcjfjt7gPYr82/X73R9FdxR/Bvc5ojxbaa1MU58fBam42lSG5FsfJ0AXHFNII1Gbvji5ecAe/y1+f50rHg8pBNLih6tBWO1uCcwQpvaT7ZOXbMHFXoK9co3167eC+36KVoN5Ef5M8ROdnQxQwmVp/J8X+qX1xaja9VWRXAd7X3tFH4YjXy6kZlb7xRLxhQTpDJcskgTLsV68oiwl+P07S6ew==
     * UserName : 来福
     */

    private List<DataListBean> DataList;

    public List<DataListBean> getDataList() {
        return DataList;
    }

    public void setDataList(List<DataListBean> DataList) {
        this.DataList = DataList;
    }

    public static class DataListBean {
        private int FansCount;
        private int Id;
        private String Photo;
        private int TopicCount;
        private String UserId;
        private String UserName;
        private int IsFocus;
        private boolean isAttention;

        public void setAttention(Boolean isAttention) {
            this.isAttention = isAttention;
        }

        public boolean getAttention() {
            return isAttention;
        }

        public int getIsFocus() {
            return IsFocus;
        }

        public void setIsFocus(int isFocus) {
            this.IsFocus = isFocus;
        }

        public int getFansCount() {
            return FansCount;
        }

        public void setFansCount(int FansCount) {
            this.FansCount = FansCount;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getPhoto() {
            return Photo;
        }

        public void setPhoto(String Photo) {
            this.Photo = Photo;
        }

        public int getTopicCount() {
            return TopicCount;
        }

        public void setTopicCount(int TopicCount) {
            this.TopicCount = TopicCount;
        }

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String UserId) {
            this.UserId = UserId;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }
    }
}
