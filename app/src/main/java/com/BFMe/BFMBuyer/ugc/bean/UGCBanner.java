package com.BFMe.BFMBuyer.ugc.bean;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/15 17:23
 */
public class UGCBanner {


    /**
     * ImageUrl : http://img01.baifomi.com//temp/image/20170217/6362294450791113288811333.jpg
     * TopicId : 100
     */

    private List<BannerBean> Banner;

    public List<BannerBean> getBanner() {
        return Banner;
    }

    public void setBanner(List<BannerBean> Banner) {
        this.Banner = Banner;
    }

    public static class BannerBean {
        private String ImageUrl;
        private long TopicId;

        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String ImageUrl) {
            this.ImageUrl = ImageUrl;
        }

        public long getTopicId() {
            return TopicId;
        }

        public void setTopicId(long TopicId) {
            this.TopicId = TopicId;
        }
    }
}
