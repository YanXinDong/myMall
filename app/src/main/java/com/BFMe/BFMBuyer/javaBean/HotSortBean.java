package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/10.
 */
public class HotSortBean {


    /**
     * ErrCode : 0
     * ResponseMsg : success
     * Data : {"HotCategory":[{"Name":"美妆香水","Id":580,"Icon":"http://img01.bfme.com/Storage/Plat/Category/201602021638595087170.jpg"},{"Name":"个护清洁","Id":448,"Icon":""},{"Name":"母婴用品","Id":338,"Icon":""},{"Name":"服饰鞋帽","Id":312,"Icon":""},{"Name":"箱包手袋","Id":500,"Icon":"http://img01.bfme.com/temp/201603161321592413440.jpg"},{"Name":"家居用品","Id":152,"Icon":"http://img01.bfme.com/temp/201603161151011972880.jpg"},{"Name":"健康营养","Id":150,"Icon":"http://img01.bfme.com/temp/201603161537233086700.jpg"},{"Name":"其他分类","Id":973,"Icon":""}]}

     */

    private String ErrCode;
    private String ResponseMsg;
    private String Data;
    /**
     * Name : 美妆香水
     * Id : 580
     * Icon : http://img01.bfme.com/Storage/Plat/Category/201602021638595087170.jpg
     */

    private List<HotCategoryBean> HotCategory;

    public String getErrCode() {
        return ErrCode;
    }

    public void setErrCode(String ErrCode) {
        this.ErrCode = ErrCode;
    }

    public String getResponseMsg() {
        return ResponseMsg;
    }

    public void setResponseMsg(String ResponseMsg) {
        this.ResponseMsg = ResponseMsg;
    }

    public String getData() {
        return Data;
    }

    public void setData(String Data) {
        this.Data = Data;
    }

    public List<HotCategoryBean> getHotCategory() {
        return HotCategory;
    }

    public void setHotCategory(List<HotCategoryBean> HotCategory) {
        this.HotCategory = HotCategory;
    }


    public static class HotCategoryBean {
        private String Name;
        private int Id;
        private String Icon;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getIcon() {
            return Icon;
        }

        public void setIcon(String Icon) {
            this.Icon = Icon;
        }
    }
}
