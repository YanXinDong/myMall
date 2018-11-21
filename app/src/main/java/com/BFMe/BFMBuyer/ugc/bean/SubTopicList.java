package com.BFMe.BFMBuyer.ugc.bean;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/19 15:41
 */
public class SubTopicList {

    /**
     * Content : 123321
     * EncryptUserId : E/YVgHAQJwd7puxKzYiKzXuZa1RzJB8twAtMFOiQzV8ofllGSGipSpzpfuMMUrczr/DQh9XT6JTy7HADMZQHNHmV+GaRmdckfJaGy4ObVirj3fSmT2vGLwssaoOBkGhbFMk9+spP/vczp1m3sAiwc2FlHvtzxr04dgrzEW4bXOqcOqHPs7bSwgDQCzPuGVkjGQaNufy5DtMGEqFTFfsja1hpSMDrzkkubVluA4lS/vVPXH1clkiid/UYb6Msbg0KyiGQSJCWLXPf1Lrw+SLUll2qVGypbO4/fRJGxnXHQaDeUq3ZZyNReVeCSHWVmSwFaOusv07Viwer0WdFMeZW5A==
     * Id : 127
     * ImageHeight : 1080
     * ImageUrl : http://192.168.1.50:8888//Storage/Plat/UGCTopicCate/108/a20a7215e28ce1d0_350.jpg
     * ImageWidth : 1920
     * IsPrase : 1
     * ParseCount : 2
     * UserId : 0
     * UserImage : http://127.0.0.1:8080//Storage/Member/1103/b09d1c88e3d22b02.jpg
     * UserName : tiger28
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
        private int UserId;
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

        public void setEncryptUserId(String EncryptUserId) {
            this.EncryptUserId = EncryptUserId;
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
