package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description：
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/6/16 11:29
 */

public class CreateTopicBean {

    /**
     * Content : 此项可以没有可以有
     * Images : ["http://192.168.1.149:8088/temp/2016042615000813332213210.png","http://192.168.1.149:8088/temp/201604261500082010231231.png","http://192.168.1.149:8088/temp/20160426150012121111.png"]
     * OrderId : ["100000000000000123","100000000000000234","100000000000000789"]
     * TopicCategoryId : 0
     * UserId : dsmediemckolkesefs
     */

    private String Content;
    private String TopicCategoryId;
    private String UserId;
    private List<String> Images;
    private List<String> OrderId;

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getTopicCategoryId() {
        return TopicCategoryId;
    }

    public void setTopicCategoryId(String TopicCategoryId) {
        this.TopicCategoryId = TopicCategoryId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public List<String> getImages() {
        return Images;
    }

    public void setImages(List<String> Images) {
        this.Images = Images;
    }

    public List<String> getOrderId() {
        return OrderId;
    }

    public void setOrderId(List<String> OrderId) {
        this.OrderId = OrderId;
    }
}
