package com.BFMe.BFMBuyer.ugc.bean;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/15 17:55
 */
public class TopicList {

    /**
     * Content : asfsafasfd
     * Id : 101
     * ImageHeight : 0
     * ImageUrl : http://192.168.1.16:8009/upload/Storage/UGC/Member/38424/111/2dc0a1239ef98419.jpg
     * ImageWidth : 0
     * IsPrase : 0
     * ParseCount : 2
     * UserId : 1819
     * UserImage : http://img01.baifomi.com//Storage/Member/40764/a65fdc08762abab6.jpeg
     * UserName : bfme_1705161047026825
     */

    private List<TopicsListBean> TopicsList;

    public List<TopicsListBean> getTopicsList() {
        return TopicsList;
    }

    public void setTopicsList(List<TopicsListBean> TopicsList) {
        this.TopicsList = TopicsList;
    }

    public static class TopicsListBean {
        private String Content;
        private String EncryptUserId;
        private long Id;
        private int ImageHeight;
        private String ImageUrl;
        private int ImageWidth;
        private int IsPrase;
        private int ParseCount;
        private int UserId;//弃用改为使用EncryptUserId
        private String UserImage;
        private String UserName;

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public String getEncryptUserId() {
            return EncryptUserId;
        }

        public void setEncryptUserId(String encryptUserId) {
            EncryptUserId = encryptUserId;
        }

        public long getId() {
            return Id;
        }

        public void setId(long Id) {
            this.Id = Id;
        }

        public int getImageHeight() {
            return ImageHeight;
        }

        public void setImageHeight(int ImageHeight) {
            this.ImageHeight = ImageHeight;
        }

        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String ImageUrl) {
            this.ImageUrl = ImageUrl;
        }

        public int getImageWidth() {
            return ImageWidth;
        }

        public void setImageWidth(int ImageWidth) {
            this.ImageWidth = ImageWidth;
        }

        public int getIsPrase() {
            return IsPrase;
        }

        public void setIsPrase(int IsPrase) {
            this.IsPrase = IsPrase;
        }

        public int getParseCount() {
            return ParseCount;
        }

        public void setParseCount(int ParseCount) {
            this.ParseCount = ParseCount;
        }

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int UserId) {
            this.UserId = UserId;
        }

        public String getUserImage() {
            return UserImage;
        }

        public void setUserImage(String UserImage) {
            this.UserImage = UserImage;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }
    }
}
