package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Description：
 * Created by :闫信董
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/7/21 11:35
 */
public class PushMainList {


    /**
     * Content : 一切都是最好的安排一切都是最好的安排一切...
     * Date : 2017-07-20
     * IsRead : 1
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
        private String Date;
        private int IsRead;

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String Date) {
            this.Date = Date;
        }

        public int getIsRead() {
            return IsRead;
        }

        public void setIsRead(int IsRead) {
            this.IsRead = IsRead;
        }
    }
}
