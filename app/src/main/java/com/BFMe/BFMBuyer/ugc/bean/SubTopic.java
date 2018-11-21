package com.BFMe.BFMBuyer.ugc.bean;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/19 15:40
 */
public class SubTopic {

    /**
     * Content : 天下之中，唯购车最贵XXXXXXXXXXXx
     * Id : 101
     * ImageUrl : http://192.168.1.50:8888//Storage/Plat/UGCTopicCate/105/4c93b890f8fdf3ea.jpg
     * IsSubscribe : 1
     * ParticipateCount : 10
     * ShareLink :
     * SubscribeCount : 1
     * Title : 晒晒你的购物车
     */

    private TopicsListBean TopicsList;

    public TopicsListBean getTopicsList() {
        return TopicsList;
    }

    public void setTopicsList(TopicsListBean TopicsList) {
        this.TopicsList = TopicsList;
    }

    public static class TopicsListBean {
        private String Content;
        private long Id;
        private String ImageUrl;
        private int IsSubscribe;
        private int ParticipateCount;
        private String ShareLink;
        private int SubscribeCount;
        private String Title;

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public long getId() {
            return Id;
        }

        public void setId(long Id) {
            this.Id = Id;
        }

        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String ImageUrl) {
            this.ImageUrl = ImageUrl;
        }

        public int getIsSubscribe() {
            return IsSubscribe;
        }

        public void setIsSubscribe(int IsSubscribe) {
            this.IsSubscribe = IsSubscribe;
        }

        public int getParticipateCount() {
            return ParticipateCount;
        }

        public void setParticipateCount(int ParticipateCount) {
            this.ParticipateCount = ParticipateCount;
        }

        public String getShareLink() {
            return ShareLink;
        }

        public void setShareLink(String ShareLink) {
            this.ShareLink = ShareLink;
        }

        public int getSubscribeCount() {
            return SubscribeCount;
        }

        public void setSubscribeCount(int SubscribeCount) {
            this.SubscribeCount = SubscribeCount;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }
    }
}
