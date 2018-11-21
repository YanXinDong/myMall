package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 * 物流详情bean
 */
public class LogisticsDetailBean {

    /**
     * ExpressInfo : [{"Content":"企业【1509009491-null】不是【跨境电商企业】企业!","Time":"/Date(1508646181276+0800)/"}]
     * ExpressNum :
     * ExpressSource : 合捷保税仓物流跟踪
     * ExpressStatus :
     * "ProductImgPath": "http://192.168.1.50:8009//Storage/Shop/520/Products/1430",
     "ProductName": "合捷保税仓测试商品",
     * SendMethod : 5
     */

    private String ExpressNum;
    private String ExpressSource;
    private String ExpressStatus;
    private String ProductImgPath;
    private String ProductName;
    private int SendMethod;
    /**
     * Content : 企业【1509009491-null】不是【跨境电商企业】企业!
     * Time : /Date(1508646181276+0800)/
     */

    private List<ExpressInfoBean> ExpressInfo;

    public String getProductImgPath() {
        return ProductImgPath;
    }

    public void setProductImgPath(String productImgPath) {
        ProductImgPath = productImgPath;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getExpressNum() {
        return ExpressNum;
    }

    public void setExpressNum(String ExpressNum) {
        this.ExpressNum = ExpressNum;
    }

    public String getExpressSource() {
        return ExpressSource;
    }

    public void setExpressSource(String ExpressSource) {
        this.ExpressSource = ExpressSource;
    }

    public String getExpressStatus() {
        return ExpressStatus;
    }

    public void setExpressStatus(String ExpressStatus) {
        this.ExpressStatus = ExpressStatus;
    }

    public int getSendMethod() {
        return SendMethod;
    }

    public void setSendMethod(int SendMethod) {
        this.SendMethod = SendMethod;
    }

    public List<ExpressInfoBean> getExpressInfo() {
        return ExpressInfo;
    }

    public void setExpressInfo(List<ExpressInfoBean> ExpressInfo) {
        this.ExpressInfo = ExpressInfo;
    }

    public static class ExpressInfoBean {
        private String Content;
        private String Time;

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public String getTime() {
            return Time;
        }

        public void setTime(String Time) {
            this.Time = Time;
        }
    }
}
