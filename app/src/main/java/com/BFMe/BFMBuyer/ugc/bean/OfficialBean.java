package com.BFMe.BFMBuyer.ugc.bean;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/7/21 12:26
 */
public class OfficialBean {

    /**
     * Id : 20170803
     * PushDate : 2017-07-20
     * Type : 1
     * messageCenter : [{"Id":177,"ImagePath":"http://192.168.1.50:8888//Storage/UGC/Member/1086/177/3c7a5d2924746fb9.jpg","ImagePathTwo":"","Title":"一切都是最好的安排一切都是最好的安排一切..."}]
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
        private String PushDate;
        private int Type;
        /**
         * Id : 177
         * ImagePath : http://192.168.1.50:8888//Storage/UGC/Member/1086/177/3c7a5d2924746fb9.jpg
         * ImagePathTwo :
         * Title : 一切都是最好的安排一切都是最好的安排一切...
         */

        private List<MessageCenterBean> messageCenter;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getPushDate() {
            return PushDate;
        }

        public void setPushDate(String PushDate) {
            this.PushDate = PushDate;
        }

        public int getType() {
            return Type;
        }

        public void setType(int Type) {
            this.Type = Type;
        }

        public List<MessageCenterBean> getMessageCenter() {
            return messageCenter;
        }

        public void setMessageCenter(List<MessageCenterBean> messageCenter) {
            this.messageCenter = messageCenter;
        }

        public static class MessageCenterBean {
            private int Id;
            private String ImagePath;
            private String ImagePathTwo;
            private String Title;

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

            public String getImagePathTwo() {
                return ImagePathTwo;
            }

            public void setImagePathTwo(String ImagePathTwo) {
                this.ImagePathTwo = ImagePathTwo;
            }

            public String getTitle() {
                return Title;
            }

            public void setTitle(String Title) {
                this.Title = Title;
            }
        }
    }
}
