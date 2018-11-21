package com.BFMe.BFMBuyer.ugc.bean;

import java.util.List;

/**
 * Description：物流推送通知
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/7/21 11:03
 */
public class PushExpressNoticeBean {

    /**
     * Content : 你的订单110112133123已完成， 祝您下次购物愉快。
     * Id : 100000
     * ImagePath : http://192.168.1.50:8888//Storage/Plat/UGCTopicCate/108/a20a7215e28ce1d0.jpg
     * OrderId : 2000000000001433
     * OrderState : 2
     * PushDate : 2017.07.19 14:32:55
     * Title : 订单已完成
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String Content;
        private int Id;
        private String ImagePath;
        private String OrderId;
        private int OrderState;
        private String PushDate;
        private String Title;
        private String IsComment;
        private String OrderStateDesc;

        public String getOrderStateDesc() {
            return OrderStateDesc;
        }

        public void setOrderStateDesc(String orderStateDesc) {
            OrderStateDesc = orderStateDesc;
        }

        public String getIsComment() {
            return IsComment;
        }

        public void setIsComment(String isComment) {
            IsComment = isComment;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
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

        public String getOrderId() {
            return OrderId;
        }

        public void setOrderId(String OrderId) {
            this.OrderId = OrderId;
        }

        public int getOrderState() {
            return OrderState;
        }

        public void setOrderState(int OrderState) {
            this.OrderState = OrderState;
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
    }
}
