package com.BFMe.BFMBuyer.ugc.bean;

import java.util.List;

/**
 * Description：话题精选
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/7/21 12:27
 */
public class TopicSelectBean {

    /**
     * Id : 20170801
     * ImagePath : http://192.168.1.50:8888//Storage/UGC/Member/1086/161/96c686ad6e0576f7.jpg
     * Photo : http://192.168.1.50:8888//Storage/Member/1086/793f093b0290fb9e.jpg
     * PushDate : 2017-07-18
     * Title : 我就是我颜色不一样的烟火
     * UserName : 锅包肉
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int Id;
        private String ImagePath;
        private String Photo;
        private String PushDate;
        private String Title;
        private String UserName;
        private long TopicId;

        public long getTopicId() {
            return TopicId;
        }

        public void setTopicId(long topicId) {
            TopicId = topicId;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getImagePath() {
            return ImagePath;
        }

        public void setImagePath(String ImagePath) {
            this.ImagePath = ImagePath;
        }

        public String getPhoto() {
            return Photo;
        }

        public void setPhoto(String Photo) {
            this.Photo = Photo;
        }

        public String getPushDate() {
            return PushDate;
        }

        public void setPushDate(String PushDate) {
            this.PushDate = PushDate;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }
    }
}
