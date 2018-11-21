package com.BFMe.BFMBuyer.ugc.bean;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/17 14:41
 */
public class UserPublishTopics {

    /**
     * Content : 冬，隔了门缝，吱呀一怀寒凉。南飞的雁，衔了离情的红枫，在别愁的枝头筑巢。最短的伫立，或许是最长的守望，魂梦悠悠，念念呼唤。
     * EncryptUserId : R3+vejpNxbBQ+vSyCSrbUem/XJyU/vyBqATNfK+6y7IM6YJVWfEoiZ3GNSFADIfHut12mQfB+BJ1nk1X+14+55NFFb/6bPkMoN8Oz7sjCDsFF2PP3BNLhuEPa3bbtewjmMfGqCeg88mQ2G0uxnZmJuIhHqcAlHR9j/TVgwy/93FkUUKge9spdc5cCpDYsq0qVU4PJx0zdtOSGVhlC4eO0WdZFUlY1VFaWYuEyn2uJcrrVFMYRO28q3wHRSkSM3MAy4s0I3XGdErBv+9vyWU6Wc513z7XVaFBkRsC8cewGONJgnc7ORrqR2vq9OHVJHlGvUt5wFdIIOEEglKPslCzjg==
     * Id : 140
     * ImageHeight : 0
     * ImageUrl :
     * ImageWidth : 0
     * IsPrase : 0
     * ParseCount : 0
     * UserId : 0
     * UserImage :
     * UserName : 我是卖家第二季
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
        private int Id;
        private int ImageHeight;
        private String ImageUrl;
        private int ImageWidth;
        private int IsPrase;
        private int ParseCount;
        private int State;
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

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
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

        public int getState() {
            return State;
        }

        public void setState(int state) {
            State = state;
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
