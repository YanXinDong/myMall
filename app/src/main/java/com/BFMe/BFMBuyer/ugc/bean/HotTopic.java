package com.BFMe.BFMBuyer.ugc.bean;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/15 17:28
 */
public class HotTopic {


    /**
     * ImageUrl : http://123.bfme.com/s/s/.png
     * ParticipateCount : 22
     * Title : 晒晒你的购物车
     * TopicId : 101
     */

    private List<HotTopicBean> HotTopic;

    public List<HotTopicBean> getHotTopic() {
        return HotTopic;
    }

    public void setHotTopic(List<HotTopicBean> HotTopic) {
        this.HotTopic = HotTopic;
    }

    public static class HotTopicBean {
        private String ImageUrl;
        private int ParticipateCount;
        private String Title;
        private long TopicId;

        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String ImageUrl) {
            this.ImageUrl = ImageUrl;
        }

        public int getParticipateCount() {
            return ParticipateCount;
        }

        public void setParticipateCount(int ParticipateCount) {
            this.ParticipateCount = ParticipateCount;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public long getTopicId() {
            return TopicId;
        }

        public void setTopicId(long TopicId) {
            this.TopicId = TopicId;
        }
    }
}
